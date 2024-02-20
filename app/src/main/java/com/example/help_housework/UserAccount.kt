package com.example.help_housework

data class UserAccount(
    var id: String?,
    var pw: String?,
    var name: String?,
    var selectedRelation: String?, // 선택된 관계
){
    constructor(): this("","","","")
}
