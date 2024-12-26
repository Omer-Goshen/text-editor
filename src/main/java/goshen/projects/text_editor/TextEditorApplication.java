package goshen.projects.text_editor;

import goshen.projects.text_editor.presentation.PresentationComponent;
import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@AllArgsConstructor
public class TextEditorApplication implements ApplicationRunner {
    private final PresentationComponent presentationComponent;

	public static void main(String[] args) {
        System.setProperty("java.awt.headless", "false");
		SpringApplication.run(TextEditorApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        presentationComponent.present();
    }
}
