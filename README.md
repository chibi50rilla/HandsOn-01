# 味ナビ (AjiNavi)

SpringBootのオンライン教材をベースに、ハンズオンで開発・機能拡張を行った飲食店口コミWebアプリケーションです。  
ローカルでの動作検証にとどまらず、AWS Lightsail（Ubuntu）へのデプロイ、Nginxによるリバースプロキシ構成、およびLet's Encryptを用いた常時SSL/TLS化（HTTPS）まで実装し、セキュアな本番運用環境を構築しています。

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

実務を見据えた堅牢性と可用性を意識し、Webサーバー（Nginx）とAPサーバー（Spring Boot）を分離したリバースプロキシ構成を採用しています。

```text
[クライアント (ブラウザ)]
       │  HTTPS (Port 443) / HTTP (Port 80 は 443 へリダイレクト)
       ▼
 [Nginx (リバースプロキシ)]  <-- Let's Encrypt (Certbot による自動更新)
       │  内部転送 (Port 8080)
       ▼
[Spring Boot (systemd)]
       │  Port 3306 (Localhost接続)
       ▼
  [MySQL 8.0]
```

### 1. 通信の暗号化（常時SSL/TLS化）
- **Let's Encrypt** のSSL/TLS証明書を導入し、全通信をHTTPS化。
- 通信の盗聴・改ざんリスクを防止し、HTTP（ポート80）へのアクセスはNginx経由で強制的にHTTPSへリダイレクト。
- **Certbot** のsystemdタイマーにより、90日ごとの証明書更新タスクを自動化。

### 2. リバースプロキシとポート秘匿化
- バックエンドのSpring Boot（8080番ポート）を外部インターネットに直接公開せず、Nginxを介した多段構成を採用。
- クラウド側のファイアウォール（AWS Lightsail）で直接の8080番ポートアクセスを遮断し、Web標準の80/443経由のみを受け付ける最小特権のアクセス制御を実施。

### 3. 可用性と耐障害性
- **systemd サービス化**: Spring BootアプリをOSデーモンとして登録（`ajinavi.service`）。クラッシュ時の自動再起動（`Restart=always`）およびサーバー再起動時の自動立ち上げを保証。
- **静的IP（Static IP）**: クラウドインスタンスのIPを固定化し、再起動に伴う接続先切断を防止。

---

## 🗄 データベース設計

### `m_restaurant` (店舗マスタ)
| カラム名 | 型 | 制約 | 説明 |
| :--- | :--- | :--- | :--- |
| `restaurant_id` | INT | PK, AUTO_INCREMENT | 店舗ID |
| `restaurant_name` | VARCHAR(255) | NOT NULL | 店舗名 |
| `catch_phrase` | VARCHAR(255) | - | キャッチフレーズ |

### `t_review` (レビューテーブル)
| カラム名 | 型 | 制約 | 説明 |
| :--- | :--- | :--- | :--- |
| `review_id` | INT | PK, AUTO_INCREMENT | レビューID |
| `restaurant_id` | INT | FK, NOT NULL | 対象店舗ID (CASCADE削除) |
| `user_id` | VARCHAR(100) | NOT NULL | 投稿ユーザーID |
| `visit_date` | DATE | NOT NULL | 訪問日 |
| `rating` | INT | NOT NULL | 評価（1〜5） |
| `comment` | TEXT | - | レビュー本文 |

---

## 💻 ローカル環境での起動手順

### 1. 前提条件
- Java 17 以上
- MySQL 8.0 以上

### 2. データベースの準備
MySQLにログインし、データベースとユーザーを作成します。

```sql
CREATE DATABASE IF NOT EXISTS aji_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER IF NOT EXISTS 'aji_user'@'localhost' IDENTIFIED BY 'aji';
GRANT ALL PRIVILEGES ON aji_db.* TO 'aji_user'@'localhost';
FLUSH PRIVILEGES;
```

### 3. リポジトリのクローンとビルド
```bash
git clone https://github.com/chibi50rilla/AjiNavi25.git
cd AjiNavi25
./mvnw clean package -DskipTests
```

### 4. アプリケーションの起動
```bash
./mvnw spring-boot:run
```

起動完了後、ブラウザで `http://localhost:8080/top` にアクセスして動作を確認します。
