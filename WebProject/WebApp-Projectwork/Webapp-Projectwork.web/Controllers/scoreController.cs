﻿using Projectwork.db.data;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using WebApplication1.Models;

namespace WebApplication1.Controllers
{
    public class scoreController : ApiController
    {
        public IHttpActionResult Get()
        {
            Dataaccess data = new Dataaccess();
            var scores = data.GetScores();

            List<scoreModel> resultscore = new List<scoreModel>();
            foreach (var item in scores)
            {
                scoreModel score = new scoreModel();
                score.id = item.id;
                score.final_score = item.final_score;
                score.month = item.month;

                resultscore.Add(score);
            }
            return Ok(resultscore);
        }
    }
}