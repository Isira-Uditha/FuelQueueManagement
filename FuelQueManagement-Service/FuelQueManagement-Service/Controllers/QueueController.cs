using FuelQueManagement_Service.Models;
using FuelQueManagement_Service.Services;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Configuration;


namespace FuelQueManagement_Service.Controllers
{
    [ApiController]
    [Route("[controller]")]
    public class QueueController : Controller
    {
        // Declearing the fuel station service instance
        private readonly QueueService _queueService;
        private readonly FuelStationService _fuelStationService;
        private readonly IConfiguration _Configuration;

        public QueueController(QueueService queueService, FuelStationService fuelStationService, IConfiguration iConfig)
        {
            _queueService = queueService;
            _fuelStationService = fuelStationService;
            _Configuration = iConfig;
        }

        // This is required to create a queue object
        [HttpPost]
        public async Task<FuelStationModel> Create(QueueModel request)
        {
            try
            {
                var res = await _queueService.Create(request);
                return res;
            }
            catch (Exception e)
            {
                return null;
            }
        }

        // This is required to delete a queue object
        [HttpDelete]
        public async Task<FuelStationModel> Delete(string fuelType, string stationId, string queueId, bool aquired)
        {
            try
            {
                if (aquired)
                {
                    QueueModel queue = new QueueModel();
                    var station = await _fuelStationService.GetStationByQueueId(queueId);
                    foreach (QueueModel que in station.Queue)
                    {
                        if (que.Id == queueId)
                        {
                            queue = que;
                        }
                    }
                    _queueService.UpdateQueueHistory(stationId, queue);
                    var currentAmount = await _fuelStationService.getCurrentFuelAmount(stationId, fuelType);
                    int letersPerVehicle = _Configuration.GetValue<int>("LetersPerVehicle");
                    _fuelStationService.ReduceFromTotalFuelAmount(stationId, letersPerVehicle, fuelType, currentAmount);
                }

                var res = await _queueService.Delete(fuelType, stationId, queueId);
                return res;

            }
            catch (Exception e)
            {
                return null;
            }
        }

        // This is required to get the station by queue Id
        [HttpGet]
        [Route("GetStation")]
        public async Task<FuelStationModel> GetStation(string queueId)
        {
            return await _fuelStationService.GetStationByQueueId(queueId);
        }

        // This is required to get the queue time
        [HttpGet]
        [Route("GetQueueTime")]
        public async Task<string> GetQueueTime(string stationId)
        {
            try
            {
                var station = await _fuelStationService.GetFuelStationById(stationId);
                var res = await _queueService.GetQueueTime(station.Queue[0]);

                return res;
            }
            catch(Exception ex)
            {
                return "";
            }

        }

        // This is required to getthe queue length
        [HttpGet]
        [Route("GetQueueLength")]
        public async Task<Array> GetQueueLength(string stationId)
        {
            try
            {
                var station = await _fuelStationService.GetFuelStationById(stationId);
                var res = await _queueService.GetQueueLength(station);
                return res;
            }
            catch (Exception ex)
            {
                return null;
            }

        }

    }

}
