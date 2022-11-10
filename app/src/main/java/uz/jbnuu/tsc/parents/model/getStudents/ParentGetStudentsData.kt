package uz.jbnuu.tsc.parents.model.getStudents

import uz.jbnuu.tsc.parents.model.me.MeData

data class ParentGetStudentsData(
    val id: Int?, // 1469,
    val auth_id: String?, // "401201100519",
    val familya: String?, // "KARIMOV",
    val ism: String?, // "SHEROZJON",
    val otasi_ismi: String?, // "SHAVKAT O‘G‘LI",
    val passport: String?, // "AB8478917",
    val JSHSHIR: String?, // "51612015540033",
    val fuqarolik: String?, // "O‘zbekiston Respublikasi fuqarosi",
    val davlat: String?, // "O‘zbekiston",
    val millat: String?, // "O‘zbeklzar",
    val viloyat: String?, // "Jizzax viloyati",
    val tuman: String?, // "Jizzax shahri",
    val jins: String?, // "Erkak",
    val tugilgan_sana: String?, // "2001-12-16",
    val group_id: Int?, // 62,
    val education_type: String?, // "Bakalavr",
    val education_shape: String?, // "Kunduzgi",
    val education_grand: String?, // "Davlat granti",
    val old_education: String?, // "2008-2019, Jizzax viloyati Jizzax shahar 6-sonli umumiy o'rta ta'lim maktabi, UM № 0070944. ",
    val student_type: String?, // "Oddiy",
    val social_type: String?, // "Boshqa",
    val count_with_person_life: String?, // "4",
    val type_with_person_life: String?, // "Oila a'zolari",
    val adress_status: String?, // "---",
    val adress: String?, // "---",
    val geo_adress: String?, // "---",
    val email: String?, // "deveprog7sh@gmail.com",
    val email_verified_at: String?, // null,
    val last_location: String?, // "{\"lat\": \"38.7142491\", \"long\": \"-9.3097433\", \"data_time\": \"2022-10-11 15:34:25\"}",
    val first_name: String?, // "e0709f89ef280e7befabcf66769d7f39",
    val role_id: Int?, // 4,
    val get_me: MeData?
)
