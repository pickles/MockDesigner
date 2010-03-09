MockDesigner 0.1

Copyright 2010 @pickles
--------------------------------------------------------------------------------
■説明
Starアプリの画面モックを作るためのツールです。
ペイントっぽく画面モックを描くことができます。
作ったモックはMockViewerアプリで携帯に表示できます。

■必要なもの
Java6 Runtime
iAppli SDK for Star-1.0以降

■起動方法
java -jar "MockDesigner.jar" 

■使い方
・ツール（画面左端のボタン）
　・Select	:コンポーネントを選択します。
　・Line	:線。
　・Border	:枠だけの箱（背景透明）。
　・Box		:枠と背景色の箱。
　・Fill	:背景色のみの箱。

・色（画面左下の２つの色）
　クリックすることで描画する色を選択できます。
　・左側：線の色
　・右側：背景色

・Selectでコンポーネントを選択すると右側に
　プロパティが表示され値を変えることができます。
　
　・Background color：背景色（RRGGBB形式）
　・Border color：線の色（RRGGBB形式）
　・Text：文字列（任意、一行だけ）
　・Text align：文字列の配置（Left/Center/Right）
　・Text color：文字列の色
　・height：高さ（Lineの場合は終点のy座標）
　・width：幅（Lineの場合は終点のx座標）
　・x：x座標
　・y：y座標
　・z：奥行（数が大きいほど手前）

・選択してDeleteキーでコンポーネントを削除できます。
・Ctrl+Cでコピー、Ctrl＋Vでペーストができます。

・Fileメニュー
　・新規：新しいキャンバスを作ります。
　　キャンバスサイズを指定することができます。
　・開く：保存したファイルを読み込みます。
