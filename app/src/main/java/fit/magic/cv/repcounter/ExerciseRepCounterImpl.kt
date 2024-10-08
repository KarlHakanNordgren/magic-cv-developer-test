// Copyright (c) 2024 Magic Tech Ltd

package fit.magic.cv.repcounter

import android.content.Context
import com.google.mlkit.vision.common.PointF3D
import fit.magic.cv.PoseLandmarkerHelper
import fit.magic.cv.repcounter.repdetect.PoseClassifierProcessor

class ExerciseRepCounterImpl : ExerciseRepCounter() {

    override fun setResults(resultBundle: PoseLandmarkerHelper.ResultBundle, context: Context) {
        val poseClassifierProcessor = PoseClassifierProcessor(context)
        val exampleLandmarks = resultBundle.results.firstOrNull()?.worldLandmarks()?.firstOrNull()
        val examplePointF3D = exampleLandmarks?.map { PointF3D.from(it.x(), it.y(), it.z()) }
        val classification = examplePointF3D?.let { poseClassifierProcessor.getPoseResult(it) }
        val lungeClassification = classification?.get("lunges")
        lungeClassification?.let { setRepCount(it.repetition) }
        lungeClassification?.let { sendProgressUpdate(it.progress) }
    }
}
