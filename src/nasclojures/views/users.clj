(ns nasclojures.views.users
  (:use [noir.core]
        hiccup.core hiccup.form)
  (:require [nasclojures.views.common :as common]
            [nasclojures.models.db :as db]
            [noir.util.crypt :as crypt]
            [noir.session :as session]
            [noir.response :as resp]))

(defpage "/signup" {:keys [handle error]}
  (common/layout
    [:div.error error]
    (form-to [:post "/signup"]
             (label "user-id" "user id")
             (text-field "handle" handle)
             [:br]
             (label "pass" "password")
             (password-field "pass")             
             [:br]
             (submit-button "create account"))))

(defpage [:post "/signup"] user
  (try 
    (db/add-user (update-in user [:pass] crypt/encrypt))
    (resp/redirect "/")
    (catch Exception ex
      (render "/signup" (assoc user :error (.getMessage ex))))))

(defpage [:post "/login"] {:keys [handle pass]}
  (render "/" 
          (let [user (db/get-user handle)] 
            (if (and user (crypt/compare pass (:pass user)))           
              (session/put! :user handle)
              {:handle handle :error "login failed"}))))

(defpage [:post "/logout"] []
  (session/clear!)
  (resp/redirect "/"))