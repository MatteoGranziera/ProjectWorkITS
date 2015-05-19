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
    public class countryController : ApiController
    {
        public IHttpActionResult Get()
        {
            Dataaccess data = new Dataaccess();
            var countries = data.GetCountries();

            List<countryModel> resultcountry = new List<countryModel>();
            foreach (var item in countries)
            {
                countryModel country = new countryModel();
                country.id = item.id;
                country.nome = item.nome;

                resultcountry.Add(country);
            }
            return Ok(resultcountry);
        }
    }
}
