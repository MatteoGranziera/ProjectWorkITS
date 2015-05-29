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

        public IEnumerable<Finalscore> GetFinalscore(Finalscore sc)
        {
            using (NpgsqlConnection connection = new NpgsqlConnection(connectionString))
            {
                connection.Open();

                string query = @"
SELECT score.id
      ,country.nome as cname
      ,languages.nome as lname
      ,score
      ,month
  FROM score
  LEFT JOIN country ON country.id = score.id_country
  LEFT JOIN languages ON languages.id = score.id_language";




                using (NpgsqlCommand command = connection.CreateCommand())
                {
                    command.CommandText = query;
                    command.CommandType = CommandType.Text;
                    bool where = false;

                    if (sc.namecountry != "Nan")
                    {
                        command.CommandText += " WHERE country.nome = @nomecountry ";
                        command.Parameters.Add(new NpgsqlParameter("@nomecountry", sc.namecountry));
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
                        command.CommandText += " languages.nome = @nomelanguage ";
                        command.Parameters.Add(new NpgsqlParameter("@nomelanguage", sc.namelanguage));
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

                    NpgsqlDataReader reader = command.ExecuteReader();

                    List<Finalscore> Scores = new List<Finalscore>();

                    while (reader.Read())
                    {
                        Finalscore finalscore = new Finalscore();

                        finalscore.id = (int)reader["id"];
                        finalscore.namecountry = reader["cname"] as string;
                        finalscore.namelanguage = reader["lname"] as string;
                        finalscore.score = (int)reader["score"];
                        finalscore.month = DateTime.ParseExact(reader["month"].ToString(), "dd/MM/yyyy hh:mm:ss" , CultureInfo.InvariantCulture);

                        Scores.Add(finalscore);
                    }
                    return Scores;
                }
            }

        }

        public IEnumerable<Finalscore> GetFinalscore()
        {
            using (NpgsqlConnection connection = new NpgsqlConnection(connectionString))
            {
                connection.Open();

                string query = @"
SELECT id
      ,id_country
      ,id_language
      ,score
      ,month
  FROM score
  LEFT JOIN country ON country.id = id_country.score
  LEFT JOIN language ON language.id = id_language.score";

                using (NpgsqlCommand command = connection.CreateCommand())
                {
                    command.CommandText = query;
                    command.CommandType = CommandType.Text;

                    NpgsqlDataReader reader = command.ExecuteReader();

                    List<Finalscore> Scores = new List<Finalscore>();

                    while (reader.Read())
                    {
                        Finalscore finalscore = new Finalscore();

                        finalscore.id = (int)reader["id"];
                        finalscore.namecountry = reader["nome"] as string;
                        finalscore.namelanguage = reader["nome"] as string;
                        finalscore.score = (int)reader["score"];
                        //finalscore.month = ["month"] as DateTime;

                        Scores.Add(finalscore);
                    }
                    return Scores;
                }
            }
        }
    }
}




