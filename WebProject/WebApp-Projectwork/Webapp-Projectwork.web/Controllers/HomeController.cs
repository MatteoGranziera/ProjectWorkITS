using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;

namespace WebApplication1.Controllers
{
    public class HomeController : Controller
    {
        public ActionResult Index()
        {
            return View();
        }


        public ActionResult Aboutus()
        {
            

            return View();
        }

        public ActionResult AboutITSKennedy()
        {
            

            return View();
        }
        
        public ActionResult ViewC(string c)
        {
            ViewBag.Country = c;
            return View();
        }
    }
}