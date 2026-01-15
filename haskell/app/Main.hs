{-# LANGUAGE OverloadedStrings #-}

module Main where

import Network.HTTP.Types.Status (forbidden403)
import Web.Scotty

main :: IO ()
main = scotty 8080 $ do
    post "/checkout" $ do
        status forbidden403
        text "User is not registered."
