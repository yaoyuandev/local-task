frontend:
	cd fe && npm run generate
	mkdir -p src/main/resources/templates
	cp fe/dist/index.html src/main/resources/templates/index.mustache
	rm -rf src/main/resources/static
	mkdir -p src/main/resources/static
	cp -r fe/dist/ src/main/resources/static/
clean:
	rm -rf src/main/resources/static
	rm -rf tmp
	rm *.gz
