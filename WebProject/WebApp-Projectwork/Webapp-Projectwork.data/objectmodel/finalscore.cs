using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Projectwork.db.data.objectmodel
{
    public class Finalscore
    {
        public int id { get; set; }
        public string namecountry { get; set; }
        public string namelanguage { get; set; }
        public int score { get; set; }
        public DateTime month { get; set; }
        public string order { get; set; }
        public bool desc { get; set; }


    }
}
