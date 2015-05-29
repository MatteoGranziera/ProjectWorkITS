using Npgsql;
using Projectwork.db.data.objectmodel;
using System;
using System.Collections.Generic;
using System.Configuration;
using System.Data;
using System.Data.SqlClient;
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

                        Scores.Add(finalscore);
                    }
                    return Scores;
                }
            }
        }
    }
}


          
        
     