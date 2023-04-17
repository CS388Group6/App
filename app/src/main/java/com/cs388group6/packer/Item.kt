package com.cs388group6.packer

import java.io.Serializable
class Item (
    var name: String = "Sony RX100 VII Camera",
    var weight: String = "420 grams",
    var image: String?,
    var category: String = "Electronics"
) : Serializable