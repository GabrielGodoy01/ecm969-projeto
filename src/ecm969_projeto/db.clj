(ns ecm969-projeto.db
  (:require [clojure.java.jdbc :as j]
            [honey.sql :as sql]
            [honey.sql.helpers :refer :all])
  )

(def mysql-db {:host "aws-sa-east-1.connect.psdb.cloud"
               :dbtype "mysql"
               :dbname "posts"
               :user "4w0i1y7ur91qkkw6i2bg"
               :password "pscale_pw_62z6IiZEoAb9Eo2HV3OnBYHXS494jJw8W5Dfa2OUJoe"})

(defn query [q] (j/query mysql-db q))

(comment
  (query ((-> (select :*)
              (from :posts)
              (sql/format))))

  )

(defn db-posts [] (-> ((j/query mysql-db
                                 ["select * from posts"]
                                 ))))