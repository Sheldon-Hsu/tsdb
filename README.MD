The goal of TSDB is to be an excellent time series database. It will have several notable features:
1. Excellent write performance. Due to the characteristics of specific business scenarios, time series databases need to have higher write performance than common databases to meet the write requirements.
2. Excellent query performance. The basic requirements of the database, in the TB\PB level of data also to achieve a millisecond level of query response.
3. Very low cost of switching database. Many business scenarios are currently implemented using postgresql, and in order to reduce the cost of switching databases, TSDB will implement standard SQL whenever possible.
4. Embedded complex analysis algorithms. The Application usually requires the use of algorithms to analyze time series data, and TSDB plans to embed more analysis algorithms.

Based on point 4 above, TSDB will become a member of the HTAP database.

If you are interested in participating in this project, please contact me. Thank you so much to everyone who has followed TSDB.