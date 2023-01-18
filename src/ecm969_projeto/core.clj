(ns ecm969-projeto.core (:require [io.pedestal.http :as http]
                                  [clojure.java.jdbc :as jdbc]
                                  [io.pedestal.http.route :as route]))

(def port 5000)

(def mysql-db {:host "aws-sa-east-1.connect.psdb.cloud"
               :dbtype "mysql"
               :dbname "posts"
               :user "4w0i1y7ur91qkkw6i2bg"
               :password "pscale_pw_62z6IiZEoAb9Eo2HV3OnBYHXS494jJw8W5Dfa2OUJoe"})

(def db-posts (jdbc/query mysql-db ["select * from posts"]))
(def db-delete (jdbc/delete! mysql-db :posts ["id = ?" 1]))
(def db-create-post (jdbc/insert! mysql-db :posts {:id "2" :text "Próximo Post" :user "2"}))

(defn get-posts [_] {:status 200 :body (println db-posts)})
(defn get-delete [_] (println db-delete) {:status 200 :body "A tabela foi limpa."})
(defn new-post [_]  (println db-create-post) {:status 200 :body "Um novo post foi criado no banco de dados!"})

(def routes (route/expand-routes #{["/delete" :get get-delete :route-name :delete]
                                   ["/insert" :get new-post :route-name :insert]
              ["/posts" :get get-posts :route-name :posts]}))

(def service-map (-> {::http/routes routes
                      ::http/type   :jetty
                      ::http/host   "0.0.0.0"
                      ::http/port   port}
                     ))

(defn -main [_] (-> service-map http/create-server http/start))

teste major