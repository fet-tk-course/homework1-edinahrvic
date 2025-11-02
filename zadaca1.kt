interface Person {
    fun getName()
    fun getTitle()
}

open class Engineer(
    val name: String,
    val surname: String,
    val dateOfBirth: String,
    val title: String,
    val experience: Int,
    val expertiseFields: String
) : Person {

    init {
        require(name.isNotBlank()) { "Name must be valid." }
        require(surname.isNotBlank()) { "Surname can't be empty." }
        require(title.isNotBlank()) { "Engineer must have a valid title." }
        require(experience >= 0) { "Experience must be zero or positive." }
        require(expertiseFields.isNotBlank()) { "Engineer must have at least one expertise field." }
    }

    override fun getName() {
        println("Engineer’s name: $name $surname")
    }

    override fun getTitle() {
        println("Engineer’s title: $title")
    }

    open fun printInfo() {
        println("$name $surname – $title – $experience godina – Ekspertize: $expertiseFields")
    }
}


class SoftwareEngineer(
    name: String,
    surname: String,
    dateOfBirth: String,
    experience: Int,
    expertiseFields: String,
    val numOfProjects: Int,
    val programmingLanguage: String
) : Engineer(name, surname, dateOfBirth, "Software engineer", experience, expertiseFields) {

    init {
        require(numOfProjects >= 0) { "Number of projects can't be negative." }
    }

    override fun printInfo() {
        println("$name $surname – $title – $experience years of experience – projects: $numOfProjects – expertises: $expertiseFields")
    }
    
    fun knowledgeLevel(){
        when(numOfProjects){
            0 -> println("Low level software engineer.")
            in 1..7 -> println("Medium level software engineer.")
            in 10..1000 -> println("High level software engineer.")
        }
    }
    
    fun favoriteLanguage(){
        println("${name}'s favorite language is ${programmingLanguage}.")
    }
}

class ElectricalEngineer(
    name: String,
    surname: String,
    dateOfBirth: String,
    experience: Int,
    expertiseFields: String,
    val numberOfCertificates: Int
) : Engineer(name, surname, dateOfBirth, "Electrical engineer", experience, expertiseFields) {

    init {
        require(numberOfCertificates >= 0) { "Number of certificates must be zero or positive." }
    }

    override fun printInfo() {
        println("$name $surname – $title – $experience years of experience – Certifikati: $numberOfCertificates – expertises: $expertiseFields")
    }
    
     fun knowledgeLevel(){
        when(numberOfCertificates){
            0 -> println("Low level electrical engineer.")
            in 1..7 -> println("Medium level electrical engineer.")
            in 10..1000 -> println("High level electrical engineer.")
        }
    }
     
   
}

fun groupEngineersByExpertise(engineers: List<Engineer>): Map<String, List<Engineer>> {
    return engineers
        .filter { it.experience > 5 } 
        .fold(mutableMapOf<String, MutableList<Engineer>>()) { acc, engineer ->
            val fields = engineer.expertiseFields.split(",").map { it.trim() }
            for (field in fields) {
                acc.getOrPut(field) { mutableListOf() }.add(engineer)
            }
            acc
        }
}

fun findMostExperiencedEngineers(engineers: List<Engineer>): Pair<SoftwareEngineer?, ElectricalEngineer?> {
    val softwareEngineers = engineers.filterIsInstance<SoftwareEngineer>()
    val electricalEngineers = engineers.filterIsInstance<ElectricalEngineer>()

    val mostExperiencedSoftware = softwareEngineers.reduce {acc, current ->
        if (current.experience > acc.experience) current else acc
    }

    val mostExperiencedElectrical = electricalEngineers.reduce { acc, current ->
        if (current.experience > acc.experience) current else acc
    }

    return Pair(mostExperiencedSoftware, mostExperiencedElectrical)
}

fun calculateTotalProjectsAndCertificates(engineers: List<Engineer>): Int {
    val softwareEngineers = engineers.filterIsInstance<SoftwareEngineer>()
    val electricalEngineers = engineers.filterIsInstance<ElectricalEngineer>()

    val totalProjects = softwareEngineers.aggregate(0) {acc, engineer -> acc + engineer.numOfProjects }
    val totalCertificates = electricalEngineers.aggregate(0) {acc, engineer -> acc + engineer.numberOfCertificates}

    val total = totalProjects + totalCertificates
    println("Total number of projects and certificates: $total")
    return total
}

fun <T> Iterable<T>.aggregate(initial: Int, operation: (acc: Int, T) -> Int): Int {
    var acc = initial
    for (element in this) acc = operation(acc, element)
    return acc
}

fun printAllEngineers(engineers: List<Engineer>) {
    println("\n=== ISPIS SVIH INŽENJERA ===")
    engineers.forEach { it.printInfo() }
}

fun main() {
    val engineers = listOf(
        SoftwareEngineer("Edina", "Hrvic", "2003-07-25", 8, "Kotlin, C++, Java", 15, "C++"),
        SoftwareEngineer("Elina", "Hrvic", "1997-10-10", 3, "Java, Databases, C#", 4, "Java"),
        ElectricalEngineer("Haris", "Delic", "1988-03-21", 8, "Power systems, PCB design", 6),
        ElectricalEngineer("Amer", "Halilovic", "1985-06-11", 11, "Embedded systems, PCB design", 10),
        SoftwareEngineer("Edin", "Hrvic", "1997-10-10", 9, "C++, React, C#", 4, "React")

    )

 
    val groups = groupEngineersByExpertise(engineers)
    for (group in groups){
        println(group.key + ": ")
        for(engineer in group.value){
            println(engineer.name + " " +  engineer.surname +", " + engineer.title + ", " + engineer.experience + " years of experience")
        }
    }
    
    
    
    
    println()
    println()
    val (softwareeng, electricaleng) = findMostExperiencedEngineers(engineers)
    println("Most experienced software engineer:" + softwareeng?.name + " " + softwareeng?.surname + ", "  + softwareeng?.experience + " years of experience")
    println("Most experienced electrical engineer:" + electricaleng?.name + " " + electricaleng?.surname + ", "  + electricaleng?.experience + " years of experience.")

    println()
    println()
    val numberOfCertificatesAndProjects = calculateTotalProjectsAndCertificates(engineers)

    println()
    println()
    println("Validity checks:")

    
    val softwareEngineers = engineers.filterIsInstance<SoftwareEngineer>()
    val electricalEngineers = engineers.filterIsInstance<ElectricalEngineer>()
    var maxExp = 0
    var softwareEng: SoftwareEngineer? = null
    for (element in softwareEngineers) {
    	if (element.experience > maxExp) {
        	maxExp = element.experience
       		softwareEng = element
    	}
    }

    maxExp = 0
    var electricalEng: ElectricalEngineer? = null
    for (element in electricalEngineers) {
    	if (element.experience > maxExp) {
        	maxExp = element.experience
        	electricalEng = element
    	}
     }

     println("Most experienced software engineer is: ${softwareEng?.name} ${softwareEng?.surname}")
     println("Most experienced electrical engineer is: ${electricalEng?.name} ${electricalEng?.surname}")


    var numOfProjects = 0
    for(element in softwareEngineers){
        numOfProjects += element.numOfProjects
    }
    var numOfCertificates = 0
    for(element in electricalEngineers){
        numOfCertificates  += element.numberOfCertificates
    }

    println("Total number of projects and certificates: " + (numOfProjects + numOfCertificates))
    
  
   println("Engineers with 5 or more years of experience: ")
   for(element in engineers){
       if(element.experience > 5){
       	println("${element?.name} ${element?.surname}")
       }
   }
}
