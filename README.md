# test-js

This is a version of Conway's game of life.
Written in Clojurescript, using the Quil library for the canvas.

The game of life code is a Clojurescript port and enhancement of Nazarii Bardiuk's 
Clojure Game of Life described here:
http://nazarii.bardiuk.com/game-of-live-clojure/

His original project is here:
https://github.com/nbardiuk/gol

More details about Conway's game of life can be found here:
http://www.conwaylife.com/wiki/Conway%27s_Game_of_Life

## Usage

Run `lein compile` command and open `index.html` in your browser.

For interactive development run `lein cljsbuild auto` command. This command will be recompiling cljs to js each time you modify `core.cljs` and you can see result immediately by refreshing page.

## License

Copyright © 2015 Nathan Smutz

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
