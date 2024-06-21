# マニュアルメモ

# ConoHaでサーバー初期構築後の処理

## SSHDサービスの変更

SSHサービスできるようにするために、SSHDサービスの設定ファイルを編集する。

```bash
# Port 16に変更
cat /etc/ssh/sshd_config
```

## Conohaのセキュリティグループのインバウンドルールの追加

ConoHa側のファイアーウォール(セキュリティグループ)を設定する。
使用するポートを開ける。

1. 自身のサーバーのネットワーク情報の最下部にセキュリティグループがある。
   以下設定。
   - IPv4v6-ICMP(Ping疎通確認)
   - IPv4v6_SSH～(2.で作成)
2. 左側のセキュリティからセキュリティグループへ移動する
   + セキュリティグループで新しいセキュリティグループを追加する。
   その後、必要なポートのインバウンドルールを追加する。
3. 1.まで戻り、作成したセキュリティグループをサーバーに付与する。

## サーバー側のファイアーウォールの設定を変更する

インバウンドについて、使用するポートのみ許可するように設定を変更する。

```bash
# ［サーバ上のadminで］ファイアウォールの現状確認
sudo ufw status verbose
# ［サーバ上のadminで］ルールを番号で表示
sudo ufw status numbered
# ［サーバ上のadminで］既存ルールを削除
sudo ufw delete 2
sudo ufw delete 1
# ［サーバ上のadminで］12345/tcp を許可
sudo ufw allow 12345/tcp
# ［サーバ上のadminで］ファイアウォールのデフォルトをdenyに
sudo ufw default deny
# ［サーバ上のadminで］ファイアウォールの現状確認
sudo ufw status verbose
```

# URL

- ConoHa

https://manage.conoha.jp/

- Supabase

# その他

https://qiita.com/plnnano/items/d55b8d76ec010bc9ce85

https://qiita.com/doraTeX/items/90452f163b17541df825
