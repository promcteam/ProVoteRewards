/**
 * 
 */
package com.caversia.plugins.votes.commons.persistence;

import static java.util.stream.Collectors.toList;

import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import com.caversia.plugins.votes.commons.model.PlayerVote;
import com.caversia.plugins.votes.commons.model.PlayerVote.Status;
import com.caversia.plugins.votes.commons.model.TopEntry;

/**
 * @author amartins
 */
public enum PersistenceManager {
    INSTANCE;

    private SessionFactory sessionsFactory;

    private PersistenceManager() {
        Thread.currentThread().setContextClassLoader(getClass().getClassLoader());
        Configuration config = new Configuration().configure();
        sessionsFactory = config.buildSessionFactory(new StandardServiceRegistryBuilder().configure().build());
    }

    public Session getSession() {
        return sessionsFactory.openSession();
    }

    public void close() {
        sessionsFactory.close();
    }

    public void persistAll(List<?> objects) {
        Session session = PersistenceManager.INSTANCE.getSession();
        try {
            Transaction transaction = session.beginTransaction();
            objects.forEach(session::persist);
            transaction.commit();
        } finally {
            session.close();
        }
    }

    public void persist(Object object) {
        Session session = PersistenceManager.INSTANCE.getSession();
        try {
            Transaction transaction = session.beginTransaction();
            session.persist(object);
            transaction.commit();
        } finally {
            session.close();
        }
    }

    public void save(Object object) {
        Session session = getSession();
        try {
            Transaction transaction = session.beginTransaction();
            session.saveOrUpdate(object);
            transaction.commit();
        } finally {
            session.close();
        }
    }

    public List<PlayerVote> getCurrentMonthVotesByPlayerNameAndStatus(String playerName, Status status) {
        return getVotesByPlayerNameAndStatusSince(playerName, status, getThisMonthStart());
    }

    @SuppressWarnings("unchecked")
    public List<PlayerVote> getVotesByPlayerNameAndStatusSince(String username, Status status, Date since) {
        Session session = PersistenceManager.INSTANCE.getSession();
        try {
            Query query = session.getNamedQuery("PlayerVote.getVotesByUsernameAndStatus");
            query.setParameter("username", username);
            query.setParameter("status", status);
            query.setParameter("startDate", since);

            return query.list();
        } finally {
            session.close();
        }
    }

    public int countCurrentMonthPlayerVotes(String username, Status status) {
        return countPlayerVotesSince(username, status, getThisMonthStart());
    }

    public PlayerVote getLatestVote(String username) {
        Session session = PersistenceManager.INSTANCE.getSession();
        try {
            Query query = session.getNamedQuery("PlayerVote.getLatestVote");
            query.setParameter("username", username);
            query.setMaxResults(1);

            return (PlayerVote) query.uniqueResult();
        } finally {
            session.close();
        }
    }

    public int countPlayerVotesSince(String username, Status status, Date since) {
        Session session = PersistenceManager.INSTANCE.getSession();
        try {
            Query query = session.getNamedQuery("PlayerVote.countPlayerVotes");
            query.setParameter("username", username);
            query.setParameter("status", status);
            query.setParameter("startDate", since);

            return ((Number) query.uniqueResult()).intValue();
        } finally {
            session.close();
        }
    }

    public int countThisMonthDistinctVoters() {
        return countDistinctVotersSince(getThisMonthStart());
    }

    public int countDistinctVotersSince(Date since) {
        StringBuilder SQL = new StringBuilder();
        SQL.append("SELECT ");
        SQL.append("    COUNT(*) ");
        SQL.append("FROM ");
        SQL.append("    ( ");
        SQL.append("        SELECT ");
        SQL.append("            COUNT(*) ");
        SQL.append("        FROM ");
        SQL.append("            players_votes v ");
        SQL.append("        WHERE ");
        SQL.append("            v.timestamp>=:startDate ");
        SQL.append("        GROUP BY ");
        SQL.append("            v.username) AS r");
        
        Session session = PersistenceManager.INSTANCE.getSession();
        try {
            SQLQuery sqlQuery = session.createSQLQuery(SQL.toString());
            sqlQuery.setParameter("startDate", since);

            return ((Number) sqlQuery.uniqueResult()).intValue();
        } finally {
            session.close();
        }
    }

    public List<TopEntry> getCurrentMonthTopTen() {
        return getCurrentMonthTop(1);
    }

    public List<TopEntry> getCurrentMonthTop(int pageNumber) {
        return getTopPageSince(getThisMonthStart(), pageNumber);
    }

    @SuppressWarnings("unchecked")
    public List<TopEntry> getTopPageSince(Date since, int pageNumber) {
        StringBuilder SQL = new StringBuilder();
        SQL.append("SELECT ");
        SQL.append("    * ");
        SQL.append("FROM ");
        SQL.append("    ( ");
        SQL.append("        SELECT ");
        SQL.append("            CAST(@row_number\\:=@row_number+1 AS INT) AS rank, ");
        SQL.append("            top.username, ");
        SQL.append("            CAST(top.votes AS INT) AS votes");
        SQL.append("        FROM ");
        SQL.append("            ( ");
        SQL.append("                SELECT ");
        SQL.append("                    @row_number\\:=0) AS r, ");
        SQL.append("            ( ");
        SQL.append("                SELECT ");
        SQL.append("                    username, ");
        SQL.append("                    COUNT(*) votes ");
        SQL.append("                FROM ");
        SQL.append("                    players_votes ");
        SQL.append("                WHERE ");
        SQL.append("                    timestamp>=:startDate ");
        SQL.append("                GROUP BY ");
        SQL.append("                    LOWER(username)) AS top ");
        SQL.append("        ORDER BY ");
        SQL.append("            top.votes DESC) AS rs");

        Session session = getSession();
        try {
            SQLQuery sqlQuery = session.createSQLQuery(SQL.toString());
            sqlQuery.setParameter("startDate", since);
            sqlQuery.setFirstResult(10 * (pageNumber - 1));
            sqlQuery.setMaxResults(10);

            //@formatter:off
             List<Object[]> results = sqlQuery.list();
             return results.stream()
                           .map(o -> new TopEntry( ((BigInteger)o[0]).intValue(), (String)o[1],  ((BigInteger)o[2]).intValue()))
                           .collect(toList());
            //@formatter:on
        } finally {
            session.close();
        }
    }

    public TopEntry getCurrentMonthPlayerRank(String playerName) {
        return getPlayerRank(getThisMonthStart(), playerName);
    }

    public TopEntry getPlayerRank(Date since, String playerName) {
        StringBuilder SQL = new StringBuilder();
        SQL.append("SELECT ");
        SQL.append("    * ");
        SQL.append("FROM ");
        SQL.append("    ( ");
        SQL.append("        SELECT ");
        SQL.append("            CAST(@row_number\\:=@row_number+1 AS INT) AS rank, ");
        SQL.append("            top.username, ");
        SQL.append("            CAST(top.votes AS INT) AS votes");
        SQL.append("        FROM ");
        SQL.append("            ( ");
        SQL.append("                SELECT ");
        SQL.append("                    @row_number\\:=0) AS c, ");
        SQL.append("            ( ");
        SQL.append("                SELECT ");
        SQL.append("                    username, ");
        SQL.append("                    COUNT(*) votes ");
        SQL.append("                FROM ");
        SQL.append("                    players_votes ");
        SQL.append("                WHERE ");
        SQL.append("                    TIMESTAMP >=:startDate ");
        SQL.append("                GROUP BY ");
        SQL.append("                    LOWER(username)) AS top ");
        SQL.append("        WHERE ");
        SQL.append("            username = :username ");
        SQL.append("        ORDER BY ");
        SQL.append("            top.votes DESC) AS ec");

        Session session = getSession();
        try {
            SQLQuery sqlQuery = session.createSQLQuery(SQL.toString());
            sqlQuery.setParameter("startDate", since);
            sqlQuery.setParameter("username", playerName);
            Object[] rank = (Object[]) sqlQuery.uniqueResult();
            if (rank != null) {
                return new TopEntry(((BigInteger) rank[0]).intValue(), (String) rank[1],
                        ((BigInteger) rank[2]).intValue());
            }
            return null;
        } finally {
            session.close();
        }
    }

    private Date getThisMonthStart() {
        Calendar monthStart = Calendar.getInstance();
        monthStart.set(Calendar.DAY_OF_MONTH, 1);
        monthStart.set(Calendar.HOUR_OF_DAY, 0);
        monthStart.set(Calendar.MINUTE, 0);

        return monthStart.getTime();
    }
}
