﻿using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;

namespace FuelQueManagement_Service.Models
{
    public class QueueModel
    {

        [BsonId]
        [BsonRepresentation(BsonType.ObjectId)]
        public string? Id { get; set; }
        public string VehicleType { get; set; }
        public string VehicleOwner { get; set; }
        public string? StationsId { get; set; }
        public string? FuelType { get; set; }
        public DateTime? ArivalTime { get; set; }
        public DateTime? DepartTime { get; set; }

        public QueueModel(string vehicleType, string owner, DateTime? arivalTime, DateTime? departTime)
        {
            VehicleType = vehicleType;
            VehicleOwner = owner;
            ArivalTime = arivalTime;
            DepartTime = departTime;
        }

        public QueueModel()
        {
        }
    }
}
