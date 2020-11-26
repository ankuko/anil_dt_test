import boto3
import csv
import sys
import argparse
import logging
import json
import psycopg2
import os

from contextlib import closing

AP = argparse.ArgumentParser()
AP.add_argument("--lcn", help="lcn")
AP.add_argument("--txn_date", help="txn_date")

SQL = """SELECT medallion,count(medallion) AS cnt
FROM public.nyc_cabdata
where medallion in ( {cab_id} )
and date_part(d, pickup_datetime) = {pickup_date}"""


def init_log():
  logging.basicConfig(
    level=logging.INFO,
    format="%(module)s %(asctime)15s %(levelname)s %(message)s", stream=sys.stdout)
  return logging.getLogger(__name__)

def get_conn():
  params = {
      'database': os.environ["ENV_REDSHIFT_DB"],
      'host': os.environ["ENV_REDSHIFT_HOST"],
      'port': os.environ["ENV_REDSHIFT_PORT"],
      'user': os.environ["ENV_REDSHIFT_USER"],
      'password': os.environ["ENV_REDSHIFT_PASS"]
  }
  conn = psycopg2.connect(**params)
  return conn


class SyncData:
  def __init__(self, lcn, txn_date):
    self.log = init_log()
    self.dbconn = get_conn()
    self.lcn = ",".join(lcn.split(","))
    self.txn_date = txn_date
    
  def get_records(self):
    with closing(self.dbconn.cursor()) as db_cursor:
      db_cursor.execute(SQL.format(lcn=self.lcn, txn_date=self.txn_date))
      result = db_cursor.fetchone()
      self.log.info(result[0])
  
  def run(self):
    self.get_records()

def main(args):
  evt = SyncData(args.lcn, args.txn_date)
  evt.run()

if __name__ == "__main__":
  main(AP.parse_args())