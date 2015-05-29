using Projectwork.db.data;
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
        public IHttpActionResult Get()
        {
            DataAccess data = new DataAccess();
            var products = data.GetFinalscore();

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
