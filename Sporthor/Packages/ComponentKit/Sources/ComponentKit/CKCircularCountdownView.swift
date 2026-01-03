//
//  CKCircularCountdownView.swift
//  ComponentKit
//
//  Created by derTurke on 22.02.2025.
//

import UIKit
import DesignKit

public final class CKCircularCountdownView: UIView {
    
    // MARK: - UI Elements
    private lazy var shapeLayer: CAShapeLayer = {
        let shapeLayer = CAShapeLayer()
        return shapeLayer
    }()
    
    private lazy var backgroundLayer: CAShapeLayer = {
        let backgroundLayer = CAShapeLayer()
        return backgroundLayer
    }()
    
    private lazy var timeLabel: CKLabel = {
        let label = CKLabel()
        return label
    }()
    
    // MARK: - Members
    private weak var delegate: CKCircularCountdownViewDelegate?
    private var countdownTimer: Timer?
    private var backgroundLayerColor: UIColor = .clear
    private var shapeLayerColor: UIColor = .clear
    private var changeShapeLayerColor: UIColor = .clear
    private var lineWidth: CGFloat = 0
    private var totalTime: Int = 0
    private var remainingTime: Int = 0
    private var changeColorDuration: Int = 0
    
    public init(delegate: CKCircularCountdownViewDelegate? = nil,
                backgroundLayerColor: UIColor = .clear,
                shapeLayerColor: UIColor = .clear,
                changeShapeLayerColor: UIColor = .clear,
                lineWidth: CGFloat = 0,
                timeTextColor: UIColor = .clear,
                timeFont: UIFont? = .systemFont(ofSize: 14),
                duration: Int = 0,
                changeColorDuration: Int = 0) {
        super.init(frame: .zero)
        self.delegate = delegate
        self.backgroundLayerColor = backgroundLayerColor
        self.shapeLayerColor = shapeLayerColor
        self.changeShapeLayerColor = changeShapeLayerColor
        self.lineWidth = lineWidth
        self.timeLabel.textColor = timeTextColor
        self.timeLabel.font = timeFont
        self.totalTime = duration
        self.remainingTime = duration
        self.changeColorDuration = changeColorDuration
        setupView()
    }
    
    required init?(coder: NSCoder) {
        fatalError("init(coder:) has not been implemented")
    }
    
    private func setupView() {
        let centerPoint = CGPoint(x: frame.size.width / 2, y: frame.size.height / 2)
        let radius: CGFloat = (min(frame.size.width, frame.size.height) - lineWidth) / 2
        let startAngle: CGFloat = -CGFloat.pi / 2
        let endAngle: CGFloat = startAngle - (2 * CGFloat.pi)

        let circularPath = UIBezierPath(arcCenter: centerPoint,
                                        radius: radius,
                                        startAngle: startAngle,
                                        endAngle: endAngle,
                                        clockwise: false)
        
        backgroundLayer.path = circularPath.cgPath
        backgroundLayer.strokeColor = backgroundLayerColor.cgColor
        backgroundLayer.lineWidth = lineWidth
        backgroundLayer.fillColor = UIColor.clear.cgColor
        layer.addSublayer(backgroundLayer)
        
        shapeLayer.path = circularPath.cgPath
        shapeLayer.strokeColor = shapeLayerColor.cgColor
        shapeLayer.lineWidth = lineWidth
        shapeLayer.fillColor = UIColor.clear.cgColor
        shapeLayer.lineCap = .round
        shapeLayer.strokeEnd = 1
        layer.addSublayer(shapeLayer)
        
        
        timeLabel.frame = bounds
        timeLabel.textAlignment = .center
        timeLabel.text = formatTime(remainingTime)
        addSubview(timeLabel)
    }
    
    public override func layoutSubviews() {
        super.layoutSubviews()
        timeLabel.frame = bounds
        setupView()
    }
    
    public func startCountdown() {
        countdownTimer = Timer.scheduledTimer(timeInterval: 1.0, target: self, selector: #selector(updateTimer), userInfo: nil, repeats: true)
    }
    
    @objc private func updateTimer() {
        if remainingTime > 0 {
            remainingTime -= 1
            timeLabel.text = formatTime(remainingTime)
            
            let progress = CGFloat(remainingTime) / CGFloat(totalTime)
            shapeLayer.strokeEnd = progress
            
            shapeLayer.strokeColor = remainingTime <= changeColorDuration ? changeShapeLayerColor.cgColor : shapeLayerColor.cgColor
        } else {
            countdownTimer?.invalidate()
            delegate?.circularCountdownViewDidFinish()
        }
    }
    
    private func formatTime(_ seconds: Int) -> String {
        let minutes = seconds / 60
        let seconds = seconds % 60
        return String(format: "%d:%02d", minutes, seconds)
    }
    
    public func configure(delegate: CKCircularCountdownViewDelegate? = nil,
                          backgroundLayerColor: UIColor = .clear,
                          shapeLayerColor: UIColor = .clear,
                          changeShapeLayerColor: UIColor = .clear,
                          lineWidth: CGFloat = 0,
                          timeTextColor: UIColor = .clear,
                          timeFont: UIFont? = .systemFont(ofSize: 14),
                          duration: Int = 0,
                          changeColorDuration: Int = 0) {
        self.delegate = delegate
        self.backgroundLayerColor = backgroundLayerColor
        self.shapeLayerColor = shapeLayerColor
        self.changeShapeLayerColor = changeShapeLayerColor
        self.lineWidth = lineWidth
        self.timeLabel.textColor = timeTextColor
        self.timeLabel.font = timeFont
        self.totalTime = duration
        self.remainingTime = duration
        self.changeColorDuration = changeColorDuration
        self.setNeedsDisplay()
        setupView()
        startCountdown()
    }
}

