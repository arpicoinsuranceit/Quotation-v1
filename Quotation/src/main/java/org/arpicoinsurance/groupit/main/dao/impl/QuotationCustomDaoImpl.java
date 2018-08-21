package org.arpicoinsurance.groupit.main.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.arpicoinsurance.groupit.main.dao.custom.QuotationCustomDao;
import org.arpicoinsurance.groupit.main.helper.QuotationSearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Repository;

@Repository
public class QuotationCustomDaoImpl implements QuotationCustomDao {

	private JdbcTemplate jdbcTemplate;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<QuotationSearch> getQuotation(String id) throws Exception {
		List<QuotationSearch> quotationSearchs = null;
		try {
			List<Object> args = new ArrayList<>();

			quotationSearchs = jdbcTemplate.query(
					"SELECT qd.qd_id, qd.quotation_id, qd.seqnum FROM quotation_details qd, quotation q "
					+ "where qd.quotation_id = q.id and q.status = 'active' and quotation_id like '"+id+"%'",
					args.toArray(), new ResultSetExtractor<List<QuotationSearch>>() {

						@Override
						public List<QuotationSearch> extractData(ResultSet resultSet)
								throws SQLException, DataAccessException {
							List<QuotationSearch> quotationSearchsTemp = new ArrayList<QuotationSearch>();
							while (resultSet.next()) {
								QuotationSearch quotationSearch = getQuoSearch(resultSet);
								quotationSearchsTemp.add(quotationSearch);
							}
							return quotationSearchsTemp;

						}

					});
		} catch (DataAccessException dataAccessException) {
			throw dataAccessException;

		}
		return quotationSearchs;
	}

	protected QuotationSearch getQuoSearch(ResultSet resultSet) throws SQLException {
		QuotationSearch quotationSearch = new QuotationSearch();
		quotationSearch.setQuotationId(Integer.toString(resultSet.getInt("quotation_id")));
		quotationSearch.setQuotationDetailId(Integer.toString(resultSet.getInt("qd_id")));
		quotationSearch.setSeqId(resultSet.getInt("seqnum"));
		return quotationSearch;
	}

	@Override
	public List<QuotationSearch> getQuotationForReceipt(String id) throws Exception {
		List<QuotationSearch> quotationSearchs = null;
		try {
			List<Object> args = new ArrayList<>();

			quotationSearchs = jdbcTemplate.query(
					"SELECT qd.qd_id, qd.quotation_id, qd.seqnum FROM quotation_details qd, quotation q "
					+ "where qd.quotation_id = q.id and q.status = 'active' and quotation_id like '"+id+"%'",
					args.toArray(), new ResultSetExtractor<List<QuotationSearch>>() {

						@Override
						public List<QuotationSearch> extractData(ResultSet resultSet)
								throws SQLException, DataAccessException {
							List<QuotationSearch> quotationSearchsTemp = new ArrayList<QuotationSearch>();
							while (resultSet.next()) {
								QuotationSearch quotationSearch = getQuoSearch(resultSet);
								quotationSearchsTemp.add(quotationSearch);
							}
							return quotationSearchsTemp;

						}

					});
		} catch (DataAccessException dataAccessException) {
			throw dataAccessException;

		}
		return quotationSearchs;
	}

}
