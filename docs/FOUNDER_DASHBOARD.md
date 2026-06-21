# 📊 Founder Dashboard

> Track these metrics from Day 1 of closed testing. Review weekly.

## 🎯 Success Metrics (Product)

| Metric | How to Measure | MVP Target | Stretch | Kill Threshold |
|--------|---------------|-----------|---------|----------------|
| D1 Retention | Firebase: user returns on day after install | >40% | >50% | <30% |
| D7 Retention | Firebase: user returns 7 days after install | >18% | >25% | <12% |
| D30 Retention | Firebase: user returns 30 days after install | >8% | >12% | <4% |
| Session Completion Rate | Firebase: session_completed / session_started | >65% | >80% | <50% |
| Avg Sessions/Active User/Day | Firebase: total_sessions / DAU | >1.3 | >2.0 | <1.0 |
| Boss 2 Reach Rate | Firebase: users who damage boss 2 / total users | >35% | >50% | <25% |
| Boss 3 Defeat Rate | Firebase: users who defeat boss 3 / total users | >15% | >25% | <8% |

## 📱 Retention Metrics

| Metric | How to Measure | MVP Target | Stretch | Notes |
|--------|---------------|-----------|---------|-------|
| D1 → D2 Return Rate | Firebase cohort analysis | >50% | >65% | First sign of stickiness |
| D7 → D8 Return Rate | Firebase cohort analysis | >60% | >75% | Weekly habit forming |
| Streak Maintenance Rate | % of users who maintain 3+ day streak | >30% | >45% | Core retention driver |
| Avg Streak Length | Room query: avg max streak per user | >3 days | >7 days | Engagement depth |
| Session Abandonment Rate | Firebase: session_abandoned / session_started | <35% | <20% | Lower is better |
| Weekly Active / Monthly Active | Firebase WAU/MAU ratio | >0.4 | >0.6 | Frequency signal |

## 💰 Monetization Metrics (Post-MVP — v1.1)

| Metric | How to Measure | Target | Notes |
|--------|---------------|--------|-------|
| Free → Paid Conversion | paying_users / total_users | >4% | Subscription launch |
| Monthly ARPU | MRR / total users | >$3.50 | After v1.1 |
| Payer ARPU | MRR / paying users | >$5.00 | After v1.1 |
| Trial → Paid Conversion | trial_converted / trial_started | >35% | After v1.1 |
| Monthly Churn Rate | lost_subscribers / total_subscribers | <8% | After v1.1 |
| LTV (Lifetime Value) | ARPU × avg subscriber lifetime | >$30 | After v1.1 |
| Lifetime Deal Conversion | lifetime_sales / total users during promo | >0.5% | Promotional periods |

## 🚀 Launch Metrics

| Metric | How to Measure | Day 14 Target | Week 2 Target | Month 1 Target |
|--------|---------------|---------------|---------------|----------------|
| Total Installs | Play Console | 12 (testers) | 50 | 200 |
| Play Store Rating | Play Console | N/A (closed) | >4.0 | >4.2 |
| Reviews Count | Play Console | 0 (closed) | >5 | >15 |
| Survey Responses | Google Form | >5 | >8 | >15 |
| "I'd pay for this" mentions | Survey + reviews + DMs | >1 | >3 | >5 |
| Organic Shares | Firebase share_clicked events | >3 | >10 | >25 |
| Reddit/X/LinkedIn engagement | Post upvotes + comments | >10 | >30 | >50 |
| Shorts views | Platform analytics | >500 | >2,000 | >10,000 |

## 📈 Growth Metrics (Post-Launch)

| Metric | How to Measure | Month 1 | Month 3 | Month 6 |
|--------|---------------|---------|---------|---------|
| Daily Installs | Play Console | 5-10 | 15-30 | 30-50 |
| Cumulative Installs | Play Console | 200 | 1,000 | 5,000 |
| DAU | Firebase | 20-40 | 100-200 | 300-500 |
| MAU | Firebase | 50-80 | 300-500 | 1,000-2,000 |
| ASO Keyword Rankings | AppTweak / manual search | Top 50 | Top 20 | Top 10 |
| Store Listing Conversion | installs / store listing visitors | >15% | >25% | >30% |

## 🔴 Kill Criteria Dashboard

**Kill the project if ANY of these are true after 100 users:**

| # | Criterion | Current Status | Threshold |
|---|-----------|---------------|-----------|
| K1 | D1 retention | ______% | < 30% |
| K2 | Session completion rate | ______% | < 50% |
| K3 | Boss 2 reach rate | ______% | < 25% |
| K4 | "I focused more than usual" survey | ______% | < 20% |
| K5 | Reviews say "just a timer with a health bar" | Yes/No | Yes |

**Decision date: ___________ (14 days after 100th user)**

## 📋 Weekly Review Template

```
Week of: _________

## Numbers This Week
- New installs: ___
- Total installs: ___
- DAU: ___
- MAU: ___
- D1 retention (this week's cohort): ___%
- D7 retention (last week's cohort): ___%
- Sessions completed: ___
- Session completion rate: ___%
- Shares: ___
- Survey responses: ___
- New reviews: ___
- Average rating: ___

## What's Working
1.
2.
3.

## What's Not Working
1.
2.
3.

## Decision This Week
[ ] Continue as-is
[ ] Adjust (what: ___________)
[ ] Kill (why: ___________)
```