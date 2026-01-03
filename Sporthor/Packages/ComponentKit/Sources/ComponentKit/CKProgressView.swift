import UIKit
import DesignKit

public final class CKProgressView: UIView {
    
    // MARK: - UI Elements
    private lazy var stackView: CKStackView = {
        let stackView = CKStackView(axis: .horizontal,
                                    distribution: .fillEqually)
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()
    
    // MARK: - Members
    private weak var delegate: CKProgressViewDelegate?
    private var stepViews: [UIView] = []
    private var numberOfSteps: Int = 0
    private var selectedStep: Int = 0
    private var activeColor: UIColor = .clear
    private var inactiveColor: UIColor = .clear
    private var stepHeight: CGFloat = .zero
    private var spacing: CGFloat = .zero
    private var duration: TimeInterval = 5.0
    private var currentAnimator: UIViewPropertyAnimator?
    private var isAnimating = false
    
    // MARK: - Initialize
    public init(steps: Int = 0,
                selectedStep: Int = 0,
                activeColor: UIColor = ColorName.backgroundPrimaryGreen.color,
                inactiveColor: UIColor = ColorName.backgroundSub300.color,
                stepHeight: CGFloat = 3.0,
                spacing: CGFloat = 8.0) {
        super.init(frame: .zero)
        bind(steps: steps,
             selectedStep: selectedStep,
             activeColor: activeColor,
             inactiveColor: inactiveColor,
             stepHeight: stepHeight,
             spacing: spacing)
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    // MARK: - Custom Methods
    public func bind(steps: Int,
                     selectedStep: Int,
                     activeColor: UIColor = ColorName.backgroundPrimaryGreen.color,
                     inactiveColor: UIColor = ColorName.backgroundSub300.color,
                     stepHeight: CGFloat = 3.0,
                     spacing: CGFloat = 8.0) {
        self.numberOfSteps = steps
        self.selectedStep = selectedStep
        self.activeColor = activeColor
        self.inactiveColor = inactiveColor
        self.stepHeight = stepHeight
        self.spacing = spacing
        reset()
        setupStackView()
        setupSteps()
        updateProgress()
    }
    
    public func bindWithAnimation(delegate: CKProgressViewDelegate? = nil,
                                  steps: Int,
                                  selectedStep: Int,
                                  activeColor: UIColor = ColorName.backgroundPrimaryGreen.color,
                                  inactiveColor: UIColor = ColorName.backgroundSub300.color,
                                  stepHeight: CGFloat = 3.0,
                                  spacing: CGFloat = 8.0,
                                  durationPerStep: TimeInterval = 5.0) {
        self.delegate = delegate
        self.numberOfSteps = steps
        self.selectedStep = selectedStep
        self.activeColor = activeColor
        self.inactiveColor = inactiveColor
        self.stepHeight = stepHeight
        self.spacing = spacing
        self.duration = durationPerStep
        reset()
        setupStackView()
        setupStepsWithAnimation()
        
        DispatchQueue.main.async { [weak self] in
            guard let self else { return }
            self.animateStep()
        }
    }

    private func setupStackView() {
        stackView.spacing = spacing
        if stackView.superview == nil {
            addSubview(stackView)
            NSLayoutConstraint.activate([
                stackView.topAnchor.constraint(equalTo: topAnchor),
                stackView.leadingAnchor.constraint(equalTo: leadingAnchor),
                stackView.trailingAnchor.constraint(equalTo: trailingAnchor),
                stackView.bottomAnchor.constraint(equalTo: bottomAnchor)
            ])
        }
    }
    
    private func updateProgress() {
        for (index, stepView) in stepViews.enumerated() {
            stepView.backgroundColor = index < selectedStep ? activeColor : inactiveColor
        }
    }
    
    private func setupSteps() {
        for _ in 0..<numberOfSteps {
            let stepView = UIView()
            stepView.backgroundColor = inactiveColor
            stepView.layer.cornerRadius = stepHeight / 2
            stepView.translatesAutoresizingMaskIntoConstraints = false
            stackView.addArrangedSubview(stepView)
            NSLayoutConstraint.activate([
                stepView.heightAnchor.constraint(equalToConstant: stepHeight)
            ])
            stepViews.append(stepView)
        }
    }
    
    private func setupStepsWithAnimation() {
        for _ in 0..<numberOfSteps {
            let stepView = UIView()
            stepView.backgroundColor = inactiveColor
            stepView.layer.cornerRadius = stepHeight / 2
            stepView.translatesAutoresizingMaskIntoConstraints = false
            stackView.addArrangedSubview(stepView)
            NSLayoutConstraint.activate([
                stepView.heightAnchor.constraint(equalToConstant: stepHeight)
            ])
            stepViews.append(stepView)
            
            let progressBar = UIView()
            progressBar.backgroundColor = activeColor
            progressBar.translatesAutoresizingMaskIntoConstraints = false
            progressBar.setCornerRadius(stepHeight / 2)
            stepView.addSubview(progressBar)
            
            NSLayoutConstraint.activate([
                progressBar.leadingAnchor.constraint(equalTo: stepView.leadingAnchor),
                progressBar.topAnchor.constraint(equalTo: stepView.topAnchor),
                progressBar.bottomAnchor.constraint(equalTo: stepView.bottomAnchor),
                progressBar.widthAnchor.constraint(equalToConstant: 0)
            ])
        }
    }

    private func animateStep() {
        guard selectedStep < stepViews.count else { return }
        
        for (index, stepView) in stepViews.enumerated() {
            if index < selectedStep {
                stepView.backgroundColor = activeColor
            } else {
                stepView.backgroundColor = inactiveColor
            }
        }
        
        let stepView = stepViews[selectedStep]
        guard let progressBar = stepView.subviews.first,
              let widthConstraint = progressBar.constraints.first(where: { $0.firstAttribute == .width }) else {
            return
        }
        
        layoutIfNeeded()
        
        currentAnimator?.stopAnimation(true)
        currentAnimator = nil

        let animator = UIViewPropertyAnimator(duration: duration, curve: .linear) { [weak self] in
            guard let self = self else { return }
            widthConstraint.constant = stepView.bounds.width
            self.layoutIfNeeded()
        }
        
        animator.addCompletion { [weak self] position in
            guard let self = self else { return }
            if position == .end {
                self.selectedStep += 1
                self.delegate?.selectedStepDidFinish(step: self.selectedStep)
            }
        }
        
        currentAnimator = animator
        animator.startAnimation()
    }

    public func setSelectedStepWithAnimation(_ selectedStep: Int,
                                             duration: TimeInterval) {
        self.selectedStep = selectedStep
        self.duration = duration
        animateStep()
    }
    
    private func reset() {
        if let animator = currentAnimator {
            if animator.state == .active || animator.state == .inactive {
                animator.stopAnimation(true)
                animator.finishAnimation(at: .current)
            }
            currentAnimator = nil
        }
        stepViews.forEach { $0.removeFromSuperview() }
        stepViews.removeAll()
        stackView.arrangedSubviews.forEach { $0.removeFromSuperview() }
    }
    
    public func pause() {
        guard let animator = currentAnimator,
              animator.isRunning else { return }
        animator.pauseAnimation()
        animator.stopAnimation(true)
        animator.finishAnimation(at: .current)
        isAnimating = false;
        currentAnimator = nil;
    }

    public func resume() {
        guard let animator = currentAnimator,
              !animator.isRunning else { return }
        currentAnimator?.startAnimation()
        isAnimating = true
    }
}
