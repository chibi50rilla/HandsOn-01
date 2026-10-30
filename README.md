# README
SpringBootのオンライン教材を用いて、ハンズオンでアプリを開発しました。
## 味ナビ (AjiNavi)

飲食店情報の検索および口コミ・レビューを管理・共有できるフルスタックWebアプリケーションです。  
クラウド環境（AWS Lightsail）にデプロイし、リバースプロキシ構成と常時SSL/TLS化（HTTPS）によるセキュアな本番運用環境を構築しています。

---

## 🌐 公開URL

- **本番環境**: [https://ajinavi-portfolio.duckdns.org/top](https://ajinavi-portfolio.duckdns.org/top)  
  *(※常時HTTPS対応 / HTTPアクセス時は自動でHTTPSへリダイレクトされます)*

---

## 🛠 技術スタック

| レイヤー | 技術 |
| :--- | :--- |
| **フロントエンド** | HTML5 / CSS3 / Thymeleaf |
| **バックエンド** | Java 17 / Spring Boot 3.x / Spring Data JDBC (JdbcTemplate) |
| **データベース** | MySQL 8.0 (InnoDB, utf8mb4) |
| **インフラ / サーバー** | AWS Lightsail (Ubuntu 22.04 LTS) |
| **Webサーバー / プロキシ** | Nginx (Reverse Proxy) |
| **セキュリティ / SSL** | Let's Encrypt / Certbot (SSL/TLS常時暗号化) |
| **プロセス管理** | systemd (バックグラウンド常駐・OS再起動時自動復旧) |
| **バージョン管理** | Git / GitHub |

---

## 📌 主な機能

- **店舗一覧・あいまい検索機能**: 店名やキャッチフレーズによる柔軟なキーワード検索
- **レビュー閲覧機能**: 店舗ごとの口コミ一覧、平均評価（レーティング）の自動算出・表示
- **レビュー投稿・管理機能**: 訪問日・評価・コメントのバリデーションおよび新規登録・更新・削除

---

## 🏗 インフラ・セキュリティ設計

本プロジェクトでは、実務を見据えた堅牢性と可用性を意識したサーバー構成を採用しています。
