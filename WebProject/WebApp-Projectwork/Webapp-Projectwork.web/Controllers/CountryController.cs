using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace WebApplication1.Controllers
{
    public class CountryController : Controller
    {
        // GET: Countr
        public ActionResult ViewC(string c)
        {
            ViewBag.Country = c;
            return View();
        }
    }
}