main:
	latexmk -pdf --shell-escape main.tex; mv main.pdf ../out/documentacion.pdf; cp ../out/documentacion.pdf ../../src/main/resources/documentacion/
clean:
	rm *.aux *.log *.fls *.pre *.out *.toc *.fdb_latexmk Graficos/*; rm -r svg-inkscape;
graficos:
	cp GraficosPre/* Graficos;
	sed -i -E 's|(<text[^>]*>)\s*([^<]+)\s*(</text>)|\1$$\2$$\3|g' Graficos/*.svg
