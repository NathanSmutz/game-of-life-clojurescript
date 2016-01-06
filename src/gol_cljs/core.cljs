(ns gol-cljs.core
  (:require [quil.core       :as q :include-macros true]
            [quil.middleware :as m]
            [clojure.set     :as t]
            #_ [dommy.core      :as d :include-macros true]
            #_ [dommy.core      :as d :refer-macros [sel sel1]]
            [dommy.core :refer-macros [sel sel1]]))

#_(-> (sel1 :#life-output)
               (d/set-text! "Dommy works"))

#_ (dommy.core/set-text! (sel1 "#life-output") "foo")

#_ (.log js/console (sel1 "#instructions"))


(defn toggle
  "Adds an element if it is not contained in the set;
   removes the element if it is in the set."
  [set element]
  (if (contains? set element)
    (disj set element)
    (conj set element)))


(defn square [[x y]]
  (let [x+1 (inc x)
        x-1 (dec x)
        y+1 (inc y)
        y-1 (dec y)]
    [[x y][x y+1][x y-1][x+1 y][x+1 y+1][x+1 y-1][x-1 y][x-1 y+1][x-1 y-1]]))

(defn neighbors [[x y]] 
  (let [x+1 (inc x)
        x-1 (dec x)
        y+1 (inc y)
        y-1 (dec y)]
    #{[x y+1][x y-1][x+1 y][x+1 y+1][x+1 y-1][x-1 y][x-1 y+1][x-1 y-1]})) ; Returns a set of a cell's neighbors

(defn live-cell?
  "Returns the cell if it survives or becomes alive, else nil"
  [live-cells candidate]
  (let [cell-alive     (live-cells candidate)
        live-neighbors (count (t/intersection live-cells 
                                              (neighbors candidate)))]
    (when (or (= live-neighbors 3)
              (and cell-alive 
                   (= live-neighbors 2)))
      candidate)))

(defn next-generation [this-generation]  
  (->> this-generation
    (into #{} (comp (mapcat square)
                    (distinct)
                    (filter #(live-cell? this-generation %))))))

(defn setup []
  "Scene setup - is called only once"
  (q/frame-rate 10)
  (q/color-mode :hsb)
  
  ;initial state
  {:life      #{[34 33] [36 33] [43 40] [45 41] [35 32] [34 34] [44 39] [44 40]
                [36 34] [43 41] [35 35]}
   :cell-size 8
   :frame     1
   :paused    false})

(defn update-state [state]
  (if (:paused state)
    state
    (-> state
      (update-in [:life]  next-generation)
      (update-in [:frame] inc))))

(defn draw [state]
  (q/background 240)
  (q/fill       180)
  (q/stroke     220)
  
  (doseq [[x y] (:life state)
          :let [w (:cell-size state)]]
    (q/rect (* w x) (* w y) w w 2))
  
  #_ (when (<= (:frame state) 117) (save-frame (str "frame-" (:frame state) ".png")))
  )

(defn mouse-wheel [state event]
  "Zooms scene by changing cell size and shift scene to keep the same poin under the cursor"
  (let [w (:cell-size state)
        nw        (max (+ (- event) w) 1)
        mx        (q/mouse-x)
        my        (q/mouse-y)
        dx        (- (quot mx w) (quot mx nw))
        dy        (- (quot my w) (quot my nw))
        next-life (set (map (fn [[x y]] [(- x dx) (- y dy)]) (:life state)))]
    (assoc state :cell-size nw :life next-life)))

(defn add-cell [state event]
  "Mouse handler to add new lives to the scene"
  (let [w (:cell-size state)
        x (quot (:x event) w)
        y (quot (:y event) w)]
    (assoc state :life (conj (:life state) [x y]))))

(defn toggle-clicked-cell [state event]
  (let [w (:cell-size state)
        x (quot (:x event) w)
        y (quot (:y event) w)]
    (update-in state [:life] toggle [x y])))

(defn drag-pan [{w :cell-size :as state} {:keys [x y p-x p-y]}]
  (let [dx        (quot (- x p-x) w)
        dy        (quot (- y p-y) w)
        translate (fn [[x y]]
                    [(+ x dx) (+ y dy)])]
    (update-in state [:life] #(into #{} (map translate) %))))

(defn mouse-dragged [state event]
  (case (:button event)
    :left   (add-cell state event)
    :center (drag-pan state event) 
    state))

(defn mouse-clicked [state event]
  (case (:button event)
    :left  (toggle-clicked-cell state event)
    :right (update-in state [:paused] not)
    state))

(defn key-handler [state event]
  (case (:key event)
    :p (-> (sel1 :#life-output)
               (dommy.core/set-text! (str (:cell-size state) (:life state))))
    :default
    )
  state)

(defn dummy [state event]
  state)

(q/defsketch example
  :title         "Conway's Game of Life"
  :host          "gol-cljs"
  :setup         setup
  :draw          draw
  :size          [600 600]
  ;:features      [:resizeable]
  :update        update-state
  :mouse-wheel   mouse-wheel
  :mouse-dragged mouse-dragged
  :mouse-clicked mouse-clicked
  :key-typed     key-handler
  ;:key-typed     dummy
  :middleware    [m/fun-mode])

(defn -main [& args])