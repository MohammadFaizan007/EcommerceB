package com.inlog.ecommerce.myaccount.model

class AccountModel {
    var id  : Int = 0;
    var title : String= ""
    var value = ""

    constructor(id: Int, title: String, value: String) {
        this.id = id
        this.title = title
        this.value = value
    }
}