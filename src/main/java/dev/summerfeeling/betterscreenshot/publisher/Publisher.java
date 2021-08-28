package dev.summerfeeling.betterscreenshot.publisher;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.summerfeeling.betterscreenshot.BetterScreenshot;
import dev.summerfeeling.betterscreenshot.Resolution;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.StringTextComponent;

import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Base64;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.BiConsumer;

/**
 * Created: 01.05.2021
 *
 * @author Summerfeeling
 */
public class Publisher {

    private static final ExecutorService THREAD_POOL = Executors.newCachedThreadPool();
    private static final JsonParser PARSER = new JsonParser();

    public static void publishScreenshot(File file, BiConsumer<Result, String> done) {
        if (file == null || !file.exists()) {
            done.accept(Result.FILE_NOT_FOUND, "Given file does not exists!");
            return;
        }

        if (!file.canRead()) {
            done.accept(Result.FILE_NOT_ACCESSIBLE, "The given file is not accessible!");
            return;
        }

        Publisher.THREAD_POOL.execute(() -> {
            Minecraft.getInstance().ingameGUI.getChatGUI().printChatMessage(new StringTextComponent("§6Uploading screenshot..."));

            try (ByteArrayOutputStream baseOutput = new ByteArrayOutputStream()) {
                // Convert .png file into base64 string
                BufferedImage original = ImageIO.read(file);

                // Scale image
                Resolution resolution = BetterScreenshot.getInstance().getResolution();
                BufferedImage scaled = original;

                if (resolution != Resolution.ORIGINAL) {
                    float scaleX = (float) resolution.getWidth() / (float) original.getWidth();
                    float scaleY = (float) resolution.getHeight() / (float) original.getHeight();

                    scaled = new BufferedImage((int) (original.getWidth() * scaleX), (int) (original.getHeight() * scaleY), BufferedImage.TYPE_INT_ARGB);

                    AffineTransform transform = new AffineTransform();
                    transform.scale(scaleX, scaleY);

                    AffineTransformOp transformOp = new AffineTransformOp(transform, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
                    scaled = transformOp.filter(original, scaled);
                }

                ImageIO.write(scaled, "png", baseOutput);

                // Establish connection to imgur
                HttpURLConnection connection = (HttpURLConnection) new URL("https://api.imgur.com/3/image").openConnection();

                connection.setRequestMethod("POST");
                connection.setConnectTimeout(5000);
                connection.setDoOutput(true);
                connection.setDoInput(true);

                // Authorize
                connection.setRequestProperty("Authorization", "Client-ID d297fd441566f99");
                connection.setRequestProperty("Connection-Type", "multipart/form-data");

                // Pass base64 picture to imgur
                OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
                writer.write("image=" + URLEncoder.encode(Base64.getEncoder().encodeToString(baseOutput.toByteArray()), "UTF-8") + "&type=" + URLEncoder.encode("base64", "UTF-8"));
                writer.flush();

                connection.setReadTimeout(5000);

                // Get imgur's answer
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    StringBuilder builder = new StringBuilder();
                    String input;
                    while ((input = reader.readLine()) != null) builder.append(input);

                    JsonObject dataObject = PARSER.parse(builder.toString()).getAsJsonObject().get("data").getAsJsonObject();
                    done.accept(Result.UPLOAD_DONE, dataObject.get("link").getAsString());
                } catch (Exception e) {
                    done.accept(Result.UPLOAD_ERROR, e.getMessage());
                    e.printStackTrace();
                }
            } catch (Exception e) {
                done.accept(Result.UPLOAD_ERROR, e.getMessage());
                e.printStackTrace();
            }
        });
    }

}
