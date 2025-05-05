./gradlew jsBrowserDistribution && \
aws s3 sync build/dist/js/productionExecutable s3://austinkucera.com/games/sudoku/ --delete --size-only
