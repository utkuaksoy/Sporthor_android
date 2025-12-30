#!/bin/sh

echo "*********************************************************"
echo "Running git pre-commit hook. Running Static analysis... "
echo "*********************************************************"

OUTPUT="/tmp/analysis-result"
EXIT_CODE=0

# Helper function to run a gradle task and print result
run_task() {
    TASK_NAME=$1
    FRIENDLY_NAME=$2
    FIX_INSTRUCTION=$3

    echo ">>> Running ${FRIENDLY_NAME}..."
    ./gradlew ${TASK_NAME} --daemon > ${OUTPUT}
    STEP_EXIT_CODE=$?

    if [ ${STEP_EXIT_CODE} -ne 0 ]; then
        cat ${OUTPUT}
        echo "❌ ${FRIENDLY_NAME} failed."
        echo "   ${FIX_INSTRUCTION}"
        EXIT_CODE=1
    else
        echo "✅ ${FRIENDLY_NAME} passed."
    fi
}

# Step 1: Run ktlintCheck
run_task "ktlintCheck" "ktlintCheck" "Please run './gradlew ktlintFormat' to fix formatting issues."

# Step 2: Run detekt
run_task "app:detekt" "detekt" "Please fix the reported detekt issues."

# Step 3: Run spotlessCheck
run_task "spotlessCheck" "spotlessCheck" "You can run './gradlew spotlessApply' to auto-fix formatting issues."

# Step 4: Run konsist-test:testDebugUnitTest
run_task ":konsist-test:testDebugUnitTest -x test" "konsist-test:testDebugUnitTest" "Please fix the test issues in konsist-test module."

# Clean up
rm ${OUTPUT}

# Final decision
if [ ${EXIT_CODE} -ne 0 ]; then
    echo "*********************************************"
    echo "       Static Analysis / Konsist Failed      "
    echo "   Please fix the above issues before commit "
    echo "*********************************************"
    exit ${EXIT_CODE}
else
    echo "*********************************************"
    echo "      Static analysis no problems found      "
    echo "*********************************************"
fi
