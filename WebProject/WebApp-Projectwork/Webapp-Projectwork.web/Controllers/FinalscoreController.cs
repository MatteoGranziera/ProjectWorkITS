using Projectwork.db.data;
using Projectwork.db.data.objectmodel;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using WebApplication1.Models;

namespace WebApplication1.Controllers
{
    public class FinalscoreController : ApiController
    {

        public IHttpActionResult Get(string country, int? limit=-1)
        {
            DataAccess data = new DataAccess();
            Finalscore fsc = new Finalscore()
            {
                id= -1,
                namecountry = country,
                namelanguage = "Nan",
                month = new DateTime(1800, 1, 1),
                score = -1
            };

            var products = data.GetFinalscore(fsc, limit);

            List<FinalscoreModel> result = new List<FinalscoreModel>();
            foreach (var item in products)
            {
                FinalscoreModel finalscore = new FinalscoreModel();
                finalscore.id = item.id;
                finalscore.namecountry = item.namecountry;
                finalscore.namelanguage = item.namelanguage;
                finalscore.score = item.score;

                result.Add(finalscore);
            }
            return Ok(result);
        }
    }
}
