# Play Store リリースチェックリスト — 苦情対応トレーナー

## ✅ 完了済み

- [x] アプリアイコン（全密度 + round variants）
- [x] Play Store用アイコン `docs/ic_launcher_512.png`（512×512）
- [x] `AndroidManifest.xml` — `android:icon` / `android:roundIcon` 設定
- [x] リリースビルド設定（R8有効化、リソース圧縮）
- [x] ProGuard ルール（`app/proguard-rules.pro`）
- [x] 署名用 keystore 生成（`complaint-trainer-release.keystore`）
- [x] `keystore.properties` — 認証情報（gitignored）
- [x] `.gitignore` — keystore/credential ファイルを除外
- [x] AAB ビルド成功（`app/build/outputs/bundle/release/app-release.aab`）
- [x] プライバシーポリシー（`docs/privacy-policy.md`）

---

## 🔲 Play Console 登録作業（手動）

### 1. Google Play Console
- [ ] [play.google.com/console](https://play.google.com/console) にアクセス
- [ ] 「アプリを作成」→ Android → 無料 → 日本語
- [ ] アプリ名: **苦情対応トレーナー**
- [ ] デフォルト言語: 日本語 (ja-JP)

### 2. ストアの掲載情報

#### アプリ情報（日本語）
```
アプリ名（30文字以内）:
苦情対応トレーナー

簡単な説明（80文字以内）:
介護スタッフ向け 苦情・カスハラ対応の反射訓練アプリ

詳細な説明（4000文字以内）:
介護現場での苦情対応・カスタマーハラスメントに、とっさに正しい返答ができるように鍛えるトレーニングアプリです。

「謝罪」「受け止め」「確認への移行」「組織対応」「境界設定」の5カテゴリの返答型を音声で練習し、即座にフィードバックを受け取れます。

【特徴】
・全24シナリオ（難易度★〜★★★）
・音声入力 または テキスト入力に対応
・NGワード検出でNGフレーズを指摘
・ユーザー辞書登録で自分の言い回しを学習

【対象ユーザー】
介護施設スタッフ、ケアマネージャー、施設管理者
```

#### グラフィックアセット（必須）
- [ ] アイコン（512×512）→ `docs/ic_launcher_512.png` 使用
- [ ] フィーチャーグラフィック（1024×500）→ **要作成**
- [ ] スクリーンショット（最低2枚）→ **要作成**
  - 推奨: シナリオ一覧、発話画面、フィードバック画面

### 3. カテゴリ・コンテンツレーティング
- [ ] カテゴリ: **教育**
- [ ] コンテンツレーティングアンケート回答（教育 → 暴力なし → 成人向けなし）
- [ ] ターゲット層: **18歳以上**（職業用アプリ）

### 4. プライバシーポリシー
- [ ] `docs/privacy-policy.md` をWeb上に公開（GitHub Pages 等）
- [ ] Play Console の「プライバシーポリシー」欄にURLを入力

### 5. AABアップロード
- [ ] Play Console → 「内部テスト」トラックを選択
- [ ] AAB（`app/build/outputs/bundle/release/app-release.aab`）をアップロード
- [ ] バージョンコード: 1 / バージョン名: 1.0 を確認

### 6. 価格・配布
- [ ] 無料
- [ ] 配布対象国: 日本のみ（または全世界）
- [ ] デバイス対象: Android 8.0以上（API 26+）

---

## 🔲 リリース前テスト（推奨）

- [ ] 内部テスト版をインストールして全シナリオ動作確認
- [ ] 音声入力 + 手動入力どちらも動作確認
- [ ] フィードバック画面の「この表現をOKとして登録」動作確認
- [ ] バックスタック（戻るボタン）の動作確認

---

## 📁 キーファイルの保管

> **⚠️ 絶対に紛失・削除しないこと**

| ファイル | 場所 | 備考 |
|---------|------|------|
| `complaint-trainer-release.keystore` | プロジェクトルート（gitignored）| クラウドにもバックアップ推奨 |
| `keystore.properties` | プロジェクトルート（gitignored）| パスワード記載 |

keystore を失うと **同じアプリIDで更新版をリリースできなくなる**。
OneDrive / Google Drive 等にもコピーしておくこと。

---

## 📋 次バージョン（v1.1）候補

- [ ] 練習記録画面（日付別ログ）
- [ ] シナリオのJSON外部化
- [ ] `Divider` → `HorizontalDivider` 置換（deprecation警告解消）
