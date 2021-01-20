package petCareApp

import scala.io.StdIn.readInt
import scala.io.StdIn.readLine
import java.sql.DriverManager
import scala.io.Source
import scala.io.BufferedSource

object Runner{
    def main(args: Array[String]) : Unit = 
    {
            
        

        var choice: Boolean = true

        while(choice)
        {
            println("\n\n\n######### WEOLCOME TO DOGGY DAY CARE ######### \n\n\n\t1. SHOW ALL DOGS\n\n\t2. SHOW ALL OWNERS\n\n\t3. SHOW ALL SITTERS\n\n")
            println("\t4. REMOVE A DOG\n\n\t5. REMOVE A SITTER\n\n\t6. UPDATE OWNER'S EMAIL\n\n\t7. UPDATE OWNER'S PHONE NUMBER\n\n")
            println("\t8. CHANGE DOG SITTER\n\n\t9. REGISTER A DOG\n\n\t10. REGISTER AN OWNER\n\n\t11. ADD A NEW SITTER\n\n")
            println("\t12. EXIT\t\n\n\n#############################################\n\n\n")

            print("\t Enter your choice: ")
            val myChoice = readInt()
            println("\n")

            myChoice match{

                case 1 => listDog()
                case 2 => listOwner() 
                case 3 => listSitter() 
                case 4 => removeDog() 
                case 5 => removeSitter() 
                case 6 => updateMail() 
                case 7 => updatePhone()  
                case 8 => updateSitter()
                case 9 => registerDog()
                case 10 => registerOwner()
                case 11 => addSitter()         
                case 12 => {
                    choice = false
                    println("\n\n\t You have exited!")
                }
                case _ => println("Invalid Selection: Must be an integer between 1-12 included")
            }


        }
        
        
        
    } 

    def listOwner():Unit = 
    {
        val conn = MyConnection.getConnection()
        val myQuery = conn.prepareStatement("SELECT * FROM petowner;")

        myQuery.execute()

        var result = myQuery.getResultSet()

        println(s"OWNER ID | FULL NAME | PHONE NUMBER | EMAIL\n")

        while(result.next())
        {
            println("\t"+result.getString("owner_id")+" | "+result.getString("first_name")+" "+result.getString("last_name")+" | "+result.getString("telephone_number")+" | "+result.getString("email")+"\n")
        }

        myQuery.close()

        conn.close()

    }

    def listDog(): Unit =
    {
        val conn = MyConnection.getConnection()
        val myQuery = conn.prepareStatement("SELECT * FROM pet;")

        myQuery.execute()

        var result = myQuery.getResultSet()

        println(s"PET ID | PET NAME | OWNER ID | SITTER ID\n")

        while(result.next())
        {
            println("\t"+result.getString("pet_id")+" | "+result.getString("pet_name")+" | "+result.getString("owner_id")+" | "+result.getString("sitter_id")+"\n")
        }

        myQuery.close()

        conn.close()

    }

    def listSitter(): Unit =
    {
        val conn = MyConnection.getConnection()
        val myQuery = conn.prepareStatement("SELECT * FROM sitter;")

        myQuery.execute()

        var result = myQuery.getResultSet()

        println(s"SITTER ID | FULL NAME\n")


        while(result.next())
        {
            println("\t"+result.getString("sitter_id")+" | "+result.getString("first_name")+" "+result.getString("last_name")+"\n")
        }

        myQuery.close()

        conn.close()

    }

    def removeDog(): Unit =
    {
        print("\t Enter Dog ID: ")
        val petID = readInt()
        println("\n")

        val conn = MyConnection.getConnection()
        val myQuery = conn.prepareStatement("DELETE FROM pet WHERE pet_id = ?;")
        myQuery.setInt(1, petID)
        
        var result: Int = myQuery.executeUpdate()
        println(s"Rows deleted: $result")                     
        

        myQuery.close()

        conn.close()

    }

    def removeSitter(): Unit =
    {
        print("\t Enter Sitter ID: ")
        val sitterID = readInt()
        println("\n")

        val conn = MyConnection.getConnection()
        val myQuery = conn.prepareStatement("DELETE FROM sitter WHERE sitter_id = ?;")
        myQuery.setInt(1, sitterID)

        var result: Int = myQuery.executeUpdate()
        println(s"Rows deleted: $result")                  
        

        myQuery.close()

        conn.close()
    }

    def updateMail(): Unit =
    {
        print("\t Enter ID: ")
        val ownerID = readInt()
        println("\n")

        print("\t Enter Email: ")
        val ownerEmail = readLine()
        println("\n")

        val conn = MyConnection.getConnection()
        val myQuery = conn.prepareStatement("UPDATE petowner set email = ? where owner_id = ?;")
        myQuery.setString(1,ownerEmail)
        myQuery.setInt(2, ownerID)

        var result: Int = myQuery.executeUpdate()
        println(s"Rows Updated: $result")                  
        

        myQuery.close()

        conn.close()

    }

    def updatePhone(): Unit =
    {
        print("\t Enter ID: ")
        val ownerID = readInt()
        println("\n")

        print("\t Enter New Telephone Number: ")
        val ownerPhone = readLine()
        println("\n")

        val conn = MyConnection.getConnection()
        val myQuery = conn.prepareStatement("UPDATE petowner set telephone_number = ? where owner_id = ?;")
        myQuery.setString(1,ownerPhone)
        myQuery.setInt(2, ownerID)

        var result: Int = myQuery.executeUpdate()
        println(s"Rows Updated: $result")                  
        

        myQuery.close()

        conn.close()
    }

    def updateSitter(): Unit =
    {
        print("\t Enter Dog ID: ")
        val petId = readInt()
        println("\n")

        print("\t Enter New Sitter ID: ")
        val sitterId = readInt()
        println("\n")

        val conn = MyConnection.getConnection()
        val myQuery = conn.prepareStatement("UPDATE pet set sitter_id = ? where pet_id = ?;")
        myQuery.setInt(1, sitterId)
        myQuery.setInt(2, petId)

        var result: Int = myQuery.executeUpdate()
        println(s"Rows Updated: $result")                  
        

        myQuery.close()

        conn.close()
    }

    def registerDog(): Unit =
    {
        
        val conn = MyConnection.getConnection()

        print("\t Enter File Name: ")
        val fileName = readLine()

        val bufferedSource = io.Source.fromFile(fileName)

        var count: Int = 0

         for(line <- bufferedSource.getLines.drop(1))
        {
            
            val tableField = line.split(",").map(_.trim)

            val myQuery = conn.prepareStatement("INSERT INTO pet (pet_name, owner_id, sitter_id) VALUES (?,?,?);")

            myQuery.setString(1,tableField(0))
            myQuery.setInt(2,tableField(1).toInt)
            myQuery.setInt(3,tableField(2).toInt)

            myQuery.execute()

            myQuery.close()

            count = count + 1
        }  
        
        bufferedSource.close()

        conn.close()

        println(s"Rows Inserted: $count")

    }

    def registerOwner(): Unit =
    {
        
        val conn = MyConnection.getConnection()

        print("\t Enter File Name: ")
        val fileName = readLine()

        val bufferedSource = io.Source.fromFile(fileName)

        var count: Int = 0

         for(line <- bufferedSource.getLines.drop(1))
        {
            
            val tableField = line.split(",").map(_.trim)

            val myQuery = conn.prepareStatement("INSERT INTO petowner (first_name, last_name, telephone_number, email) VALUES (?,?,?,?);")

            myQuery.setString(1,tableField(0))
            myQuery.setString(2,tableField(1))
            myQuery.setString(3,tableField(2))
            myQuery.setString(4,tableField(3))

            myQuery.execute()

            myQuery.close()

            count = count + 1
        }  
        
        bufferedSource.close()

        conn.close()

        println(s"Rows Inserted: $count")
    }

    def addSitter(): Unit = 
    {
        
        val conn = MyConnection.getConnection()

        print("\t Enter File Name: ")
        val fileName = readLine()

        val bufferedSource = io.Source.fromFile(fileName)

        var count: Int = 0

         for(line <- bufferedSource.getLines.drop(1))
        {
            
            val tableField = line.split(",").map(_.trim)

            val myQuery = conn.prepareStatement("INSERT INTO sitter (first_name, last_name) VALUES (?,?);")

            myQuery.setString(1,tableField(0))
            myQuery.setString(2,tableField(1))
            
            myQuery.execute()

            myQuery.close()

            count = count + 1
        }  
        
        bufferedSource.close()

        conn.close()

        println(s"Rows Inserted: $count")
    }
}