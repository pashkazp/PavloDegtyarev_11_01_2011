package depavlo.mediaprocess;

//import org.springframework.boot.Banner;
import static java.lang.System.exit;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import depavlo.mediaprocess.service.ContentItemProcessor;
import depavlo.mediaprocess.service.ContentItemService;
import depavlo.mediaprocess.service.model.ContentItem;
import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Configuration
@EnableAutoConfiguration
@ComponentScan({ "depavlo" })
@EnableJpaRepositories({ "depavlo" })
@EntityScan({ "depavlo" })
@Slf4j
public class MediaProcessApplication implements CommandLineRunner {
	@Autowired
	private ContentItemService s;
	private ContentItemProcessor processor;

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(MediaProcessApplication.class);
//		app.setBannerMode(Banner.Mode.OFF);
		app.run(args);
	}

	@Override
	public void run(String... args) throws Exception {
		log.debug("run] {}", "=".repeat(40));
		System.out.print("Результат работы алгоритма:");
		var items = s.getItemsMap();

		if (log.isDebugEnabled()) {
			items.forEach((k, v) -> {
				log.debug("run] {}", v.getName());
			});
		}

		Optional<ContentItem> root = s.convertToTree(items);
		if (root.isPresent()) {
			processor = new ContentItemProcessor(root.get(), items);
			processor.normalizeNames();
			if (log.isDebugEnabled()) {
				log.debug("run] {} normalized", "=".repeat(40));
				items.forEach((k, v) -> {
					log.debug("run] {}:{}", "*".repeat(v.getLevel()), v.getName());
				});
			}
			processor.foldContentItemTree();
			if (log.isDebugEnabled()) {
				log.debug("run] {} folded", "=".repeat(40));
				items.forEach((k, v) -> {
					log.debug("run] {}:{} - {}", "*".repeat(v.getLevel()), v.getName(), v.getStatus());
				});
			}
		}

		List<String> copiedList = new LinkedList<>();
		List<String> codedList = new LinkedList<>();
		items.values().forEach(v -> {
			switch (v.getStatus()) {
			case CODED:
				codedList.add(v.getQualifiedName());
				break;
			case COPIED:
				copiedList.add(v.getQualifiedName());
				break;

			default:
				break;
			}
		});

		System.out.println("\nСписок файлов\\директорий на кодирование:");
		codedList.forEach(v -> System.out.println(v));
		System.out.println("\nСписок файлов\\директорий на копирование:");
		copiedList.forEach(v -> System.out.println(v));

		exit(0);

	}

}
