# Codex レビュー依頼 — complaint-trainer

## プロジェクト概要
介護現場向け 苦情・カスハラ対応の**反射訓練アプリ**（Android / Jetpack Compose）。
「正しい答えを覚える」のではなく、**言い訳したくなる瞬間に短い型へ戻る反射**を鍛えることが目的。

GitHub: https://github.com/melofla8os-ai/complaint-trainer

---

## 技術スタック
- Kotlin + Jetpack Compose (minSdk 26, compileSdk 34)
- Navigation Compose / AndroidViewModel + StateFlow
- 音声入力: `RecognizerIntent` (ja-JP)
- 採点: ルールベース（辞書マッチ）+ SharedPreferences（ユーザー登録辞書）

---

## ファイル構成

```
app/src/main/java/com/melof/complainttrainer/
├── MainActivity.kt                  # NavHost（3画面）
├── data/
│   ├── Model.kt                     # ResponseCategory enum / Scenario / ScoreResult
│   ├── SynonymDictionary.kt         # 採点用辞書（5カテゴリ + NGワード）
│   ├── ScoringEngine.kt             # 採点ロジック
│   ├── ScenarioRepository.kt        # シナリオ24件（難易度1〜3）
│   └── UserDictionaryStore.kt       # ユーザー登録辞書（SharedPreferences）
├── viewmodel/
│   └── TrainerViewModel.kt          # AndroidViewModel
└── ui/
    ├── ScenarioListScreen.kt        # シナリオ一覧
    ├── SpeakScreen.kt               # 音声入力 + 手動編集
    ├── FeedbackScreen.kt            # 採点結果 + 表現登録ボタン
    └── theme/Theme.kt
```

---

## 採点の仕組み

### ResponseCategory（5種）
| カテゴリ | label | 目的 |
|---------|-------|------|
| APOLOGY | 謝罪 | 「申し訳」「お詫び」「心苦しく」等 |
| ACCEPTANCE | 受け止め | 「承りました」「受け止めます」等 |
| CONFIRMATION | 確認への移行 | 「確認します」「経緯を確認」等 |
| ORGANIZATION | 組織対応 | 「事業所として」「責任者」等 |
| BOUNDARY_SETTING | 境界設定 | 「できかねます」「お答えできません」等 |

### 採点フロー（ScoringEngine.kt）
1. 入力テキストを辞書キーワードでマッチ（前方一致ではなく `contains`）
2. ユーザー登録フレーズも合算して判定
3. NGワード検出（でも・仕方ない・落ち着いてください 等24語）
4. 80文字超で「長め」警告
5. スコア = 達成カテゴリ数 - NGワード数 - 長め1点

### ユーザー辞書（UserDictionaryStore.kt）
- フィードバック画面で ❌ カテゴリに「この表現をOKとして登録する」ボタンが出る
- 登録したフレーズ（返答テキスト全体）を SharedPreferences に保存
- 次回採点から反映

---

## 画面フロー
```
シナリオ一覧 → 発話画面（音声入力 or テキスト入力） → フィードバック
                                                       ↓
                                                    もう一度（発話画面へ）
                                                    シナリオ一覧へ
```

---

## シナリオ（24件）
| 難易度 | 件数 | 例 |
|--------|------|---|
| ★ 軽い苦情 | 9件 | 待ち時間・声かけ・私物・請求書・入浴順 等 |
| ★★ こじれ | 6件 | 説明不足・不適切発言・電話での堂々巡り 等 |
| ★★★ 過大要求 | 9件 | SNS拡散示唆・慰謝料要求・居座り・人事要求 等 |

各シナリオに `targetCategories`（必須スロット）と `hint` を持つ。

---

## レビューで見てほしいポイント

### 1. 採点ロジックの精度・穴
- `SynonymDictionary.kt` の辞書に抜け漏れや誤検知がないか
- NGワードが誤検知しやすい表現になっていないか（例：「難しいです」が境界設定に入っているが、謝罪文脈でも使われる）
- `contains` マッチの粒度（短すぎるキーワードが誤検知源になっていないか）

### 2. ユーザー辞書の設計
- `UserDictionaryStore.addPhrase` は「返答全文」を登録するが、次回以降は部分一致で判定される
- 長い文章を登録すると次回一致しにくい問題がある
- 改善案があればほしい

### 3. ScoreResult.totalScore の計算
- 現状: `達成カテゴリ数 - NGワード数 - 長め1点`（最低0）
- 必須カテゴリのみカウントすべきか、全カテゴリをカウントすべきか要検討

### 4. コード品質・Android慣習
- ViewModelの設計、Compose の状態管理に問題がないか
- `remember { mutableStateOf(false) }` が `FeedbackScreen` の `CategoryRow` 内にある（リコンポーズで登録状態がリセットされる可能性）

### 5. 将来実装の見通し
以下を追加予定。実装上の問題点があれば指摘してほしい。
- 練習記録画面（日付別ログ、LocalDB or SharedPreferences）
- シナリオのJSON外部化（将来的に追加しやすく）
- iOS展開（ロジック層の共通化）

---

## 既知の問題・制約
- 音声認識は `RecognizerIntent`（Googleエンジン）を使用。介護敬語の誤変換が発生するため、ユーザーが手動修正してから採点する設計にした
- Claude API は現時点で使用していない（マネタイズ前はコスト不可のため）

---

## ローカル確認方法
Android Studio で `C:\Users\melof\complaint-trainer` を開き、Gradle sync 後に ▶ で実行。
