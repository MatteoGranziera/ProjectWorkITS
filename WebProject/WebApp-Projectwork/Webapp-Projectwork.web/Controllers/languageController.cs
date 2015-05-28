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
    public class languageController : ApiController
    {
        public IHttpActionResult Get()
        {
            Dataaccess data = new Dataaccess();
            var languages = data.GetLanguages();

            List<languageModel> resultlanguage = new List<languageModel>();
            foreach (var item in languages)
            {
                languageModel language = new languageModel();
                language.id = item.id;
                language.nome = item.nome;

                resultlanguage.Add(language);
            }
            return Ok(resultlanguage);
        }
    }
}
