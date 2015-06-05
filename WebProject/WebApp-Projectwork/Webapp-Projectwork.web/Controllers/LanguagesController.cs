using Projectwork.db.data;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;

namespace WebApplication1.Controllers
{
    public class LanguagesController : ApiController
    {
        public IHttpActionResult Get()
        {
            DataAccess data = new DataAccess();

            List<String> result = data.GetLanguages();

            return Ok(result);


        }
    }
}
