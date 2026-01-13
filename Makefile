.PHONY: test-spring-boot-hex clean-spring-boot-hex test-spring-boot-adm clean-spring-boot-adm

# Spring Boot Hex acceptance tests
test-spring-boot-hex: clean-spring-boot-hex
	@echo "Starting services for spring-boot-hex acceptance tests..."
	docker-compose --project-directory . -f acceptance/docker-compose.yml -f spring-boot-hex/docker-compose.yml -p acceptance up -d --build
	@echo "Waiting for services to be ready..."
	sleep 10
	@echo "Running acceptance tests..."
	cd acceptance && ./gradlew test --rerun-tasks || (cd .. && $(MAKE) clean-spring-boot-hex && exit 1)
	@echo "Tests completed successfully!"
	$(MAKE) clean-spring-boot-hex

clean-spring-boot-hex:
	@echo "Cleaning up spring-boot-hex services..."
	docker-compose --project-directory . -f acceptance/docker-compose.yml -f spring-boot-hex/docker-compose.yml -p acceptance down -v || true

# Spring Boot ADM acceptance tests (placeholder for future implementation)
test-spring-boot-adm:
	@echo "Spring Boot ADM acceptance tests not yet implemented"
	@echo "TODO: Add Dockerfile and update docker-compose.yml for spring-boot-adm"
	exit 1

clean-spring-boot-adm:
	@echo "Cleaning up spring-boot-adm services..."
	docker-compose -f acceptance/docker-compose.yml -f spring-boot-adm/docker-compose.yml -p acceptance down -v || true
