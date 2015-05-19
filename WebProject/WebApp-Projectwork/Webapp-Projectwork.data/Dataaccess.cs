using Npgsql;
using Projectwork.db.data.objectmodel;
using System;
using System.Collections.Generic;
using System.Configuration;
using System.Data;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Projectwork.db.data
{
    public class Dataaccess
    {
        string connectionString;

        public Dataaccess()
        {
            this.connectionString = ConfigurationManager.ConnectionStrings["cs"].ConnectionString;
        }


        public IEnumerable<country> GetCountries()
        {
            using (NpgsqlConnection connection = new NpgsqlConnection(connectionString))
            {
                connection.Open();

                string query = @"
SELECT id
      ,nome
  FROM country";

                using (NpgsqlCommand command = connection.CreateCommand())
                {
                    command.CommandText = query;
                    command.CommandType = CommandType.Text;

                    NpgsqlDataReader reader = command.ExecuteReader();

                    List<country> Countries = new List<country>();

                    while (reader.Read())
                    {
                        country country = new country();

                        country.id = (int)reader["id"];
                        country.nome = reader["nome"] as string;


                        Countries.Add(country);
                    }
                    return Countries;
                }
            }
        }



        public IEnumerable<language> GetLanguages()
        {
            using (NpgsqlConnection connection = new NpgsqlConnection(connectionString))
            {
                connection.Open();

                string query = @"
SELECT id
      ,nome
  FROM languages";

                using (NpgsqlCommand command = connection.CreateCommand())
                {
                    command.CommandText = query;
                    command.CommandType = CommandType.Text;

                    NpgsqlDataReader reader = command.ExecuteReader();

                    List<language> Languages = new List<language>();

                    while (reader.Read())
                    {
                        language language = new language();

                        language.id = (int)reader["id"];
                        language.nome = reader["nome"] as string;


                        Languages.Add(language);
                    }
                    return Languages;
                }
            }
        }




        public IEnumerable<score> GetScores()
        {
            using (NpgsqlConnection connection = new NpgsqlConnection(connectionString))
            {
                connection.Open();

                string query = @"
SELECT id
      ,score
      ,month
  FROM score";

                using (NpgsqlCommand command = connection.CreateCommand())
                {
                    command.CommandText = query;
                    command.CommandType = CommandType.Text;

                    NpgsqlDataReader reader = command.ExecuteReader();

                    List<score> Scores = new List<score>();

                    while (reader.Read())
                    {
                        score score = new score();

                        score.id = (int)reader["id"];
                        score.final_score = (int)reader["score"];
                        score.month = (DateTime)reader["month"];


                        Scores.Add(score);
                    }
                    return Scores;
                }
            }
        }
    }
}

