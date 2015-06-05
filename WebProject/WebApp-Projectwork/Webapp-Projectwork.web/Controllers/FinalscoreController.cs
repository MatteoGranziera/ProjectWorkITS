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

        public IHttpActionResult Get(string country=null , int? m = null, int? y = null, string lang=null, string order=null, bool? desc=null, int? limit = -1)
        {
            DataAccess data = new DataAccess();
            Finalscore fsc = new Finalscore()
            {
                id= -1,
                namecountry = (country!= null?country:"Nan"),
                namelanguage = (lang!= null?lang:"Nan"),
                month = (m != null&& y != null?new DateTime((int)y, (int)m+1, 1):new DateTime(1800, 1, 1)),
                score = -1,
                order = (order!= null?order:"Nan"),
                desc = (desc != null?(bool)desc : false)

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
                finalscore.month = item.month;

                result.Add(finalscore);
            }
            return Ok(result);
        }

       
    }
}
