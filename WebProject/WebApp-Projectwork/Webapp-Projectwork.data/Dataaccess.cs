using Npgsql;
using Projectwork.db.data.objectmodel;
using System;
using System.Collections.Generic;
using System.Configuration;
using System.Data;
using System.Data.SqlClient;
using System.Globalization;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Projectwork.db.data
{
    public class DataAccess
    {

        string connectionString;

        public DataAccess()
        {

            this.connectionString = ConfigurationManager.ConnectionStrings["cs"].ConnectionString;
        }

        public IEnumerable<Finalscore> GetFinalscore(Finalscore sc, int? limit)
        {
            using (NpgsqlConnection connection = new NpgsqlConnection(connectionString))
            {
                connection.Open();

                string query = @"
SELECT score.id
      ,countries.name as cname
      ,languages.name as lname
      ,score 
      ,month
  FROM score
  LEFT JOIN countries ON countries.id = score.id_country
  LEFT JOIN languages ON languages.id = score.id_language";




                using (NpgsqlCommand command = connection.CreateCommand())
                {
                    command.CommandText = query;
                    command.CommandType = CommandType.Text;
                    bool where = false;

                    if (sc.namecountry != "Nan")
                    {
                        command.CommandText += " WHERE countries.name = @namecountry ";
                        command.Parameters.Add(new NpgsqlParameter("@namecountry", sc.namecountry));
                        where = true;
                    }

                    if (sc.namelanguage != "Nan")
                    {
                        if (where)
                        {
                            command.CommandText += "AND";
                        }
                        else
                        {
                            command.CommandText += " WHERE";
                        }
                        command.CommandText += " languages.name = @namelanguage ";
                        command.Parameters.Add(new NpgsqlParameter("@namelanguage", sc.namelanguage));
                    }

                    if (sc.month != new DateTime(1800, 1, 1))
                    {
                        if (where)
                        {
                            command.CommandText += "AND";
                        }
                        else
                        {
                            command.CommandText += " WHERE";
                        }
                        command.CommandText += " score.month = @scoremonth ";
                        command.Parameters.Add(new NpgsqlParameter("@scoremonth", sc.month));
                    }
                    if (sc.order != "Nan")
                    {
                        command.CommandText += " ORDER BY " + sc.order;
                        

                        if (sc.desc)
                        {
                            command.CommandText += " DESC";
                        }
                        else
                        {
                            command.CommandText += " ASC";
                        }

                    }


                    if (limit != null && limit > 0)
                    {
                        command.CommandText += " LIMIT @limit";
                        command.Parameters.Add(new NpgsqlParameter("@limit", limit));
                    }





                    NpgsqlDataReader reader = command.ExecuteReader();

                    List<Finalscore> Scores = new List<Finalscore>();

                    while (reader.Read())
                    {
                        Finalscore finalscore = new Finalscore();

                        finalscore.id = (int)reader["id"];
                        finalscore.namecountry = reader["cname"] as string;
                        finalscore.namelanguage = reader["lname"] as string;
                        finalscore.score = (int)reader["score"];
                        finalscore.month = DateTime.ParseExact(reader["month"].ToString(), "dd/MM/yyyy hh:mm:ss", CultureInfo.InvariantCulture);

                        Scores.Add(finalscore);
                    }
                    return Scores;
                }
            }

        }


        public List<String> GetLanguages()
        {
            using (NpgsqlConnection connection = new NpgsqlConnection(connectionString))
            {
                connection.Open();

                string query = @"
SELECT name FROM languages ORDER BY name";

                using (NpgsqlCommand command = connection.CreateCommand())
                {
                    command.CommandText = query;
                    command.CommandType = CommandType.Text;



                    NpgsqlDataReader reader = command.ExecuteReader();

                    List<String> Languages = new List<String>();

                    while (reader.Read())
                    {
                        Languages.Add(reader["name"].ToString());
                    }
                    return Languages;
                }

            }
        }
    }
}








