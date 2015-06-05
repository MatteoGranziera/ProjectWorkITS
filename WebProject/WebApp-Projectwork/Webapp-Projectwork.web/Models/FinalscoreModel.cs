using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace WebApplication1.Models
{
    public class FinalscoreModel
    {
        public int id { get; set; }
        public string namecountry { get; set; }
        public string namelanguage { get; set; }
        public int score { get; set; }
        public DateTime month { get; set; }
    }
}