(ns ecm969-projeto.core (:require [io.pedestal.http :as http]
                                  [environ.core :refer [env]]
                                  [ecm969-projeto.db :refer [db-posts]]
                                  [io.pedestal.http.route :as route]))

(def posts [{:id "0" :text "Este é meu primeiro Post!" :user "0"}
             {:id "1" :text "Que plataforma incrível" :user ""}
             {:id "2" :text "Odeio este Gabriel!!!" :user "1"}
             {:id "3" :text "Clojure reina!!!!!!!!!!!" :user "1"}
             {:id "4" :text "Obrigado Cidinho!" :user "1"}])

(defn get-post [{{:keys [post]} :path-params
                 {:keys [extended]} :query-params}]
  (if-let [post (->> posts
                     (filter #(= post (:id %)))
                     first)]
    {:status 200 :body (if extended post (dissoc post :id))}
    {:status 404}))

(defn get-posts [_] {:status 200 :body db-posts})

(def routes #{["/posts/:post" :get get-post :route-name :get-post]
              ["/posts" :get get-posts :route-name :get-posts]})

(def service-map (-> {::http/routes routes
                      ::http/type   :immutant
                      ::http/host   "0.0.0.0"
                      ::http/join?  false
                      ::http/port   (Integer. (or (env :port) 5000))}
                     http/default-interceptors
                     (update ::http/interceptors into [http/json-body])))

(defn -main [_] (-> service-map http/create-server http/start))
