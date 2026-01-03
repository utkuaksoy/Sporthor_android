//
//  CKAddPersonInfoAndButtonView.swift
//  ComponentKit
//
//  Created by GÜRHAN YUVARLAK on 29.10.2025.
//

import UIKit
import DesignKit

public protocol CKAddPersonInfoAndButtonViewDelegate: AnyObject {
    func didTappedCKAddPersonInfoAndButtonView(_ tag: Int)
}

public final class CKAddPersonInfoAndButtonView: UIView {
    private lazy var titleLabel: CKLabel = {
        let label = CKLabel(textColor: ColorName.borderStrong900.color,
                            numberOfLines: 0,
                            font: .bold03Compact)
        return label
    }()
    
    private lazy var infoLabel: CKLabel = {
        let label = CKLabel(textColor: ColorName.contentSoft600.color,
                            numberOfLines: 0,
                            font: .body04Compact)
        return label
    }()
    
    private lazy var labelStackView: CKStackView = {
        let stackView = CKStackView(spacing: 8)
        stackView.addArrangedSubviews([titleLabel, infoLabel])
        return stackView
    }()
    
    private lazy var button: CKButton = {
        let button = CKButton(titleColor: ColorName.contentWhite0.color,
                              buttonBackgroundColor: ColorName.contentStrong900.color,
                              cornerRadius: 16,
                              font: .bold04Compact)
        button.heightAnchor.constraint(equalToConstant: 38).isActive = true
        button.contentEdgeInsets = .init(top: 0, left: 21, bottom: 0, right: 21)
        button.addTarget(self, action: #selector(didTappedButton(_:)), for: .touchUpInside)
        return button
    }()
    
    private lazy var buttonSpacerView: UIView = {
        let view = UIView()
        view.backgroundColor = .clear
        return view
    }()
    
    private lazy var buttonStackView: CKStackView = {
        let stackView = CKStackView(axis: .horizontal, alignment: .center)
        stackView.addArrangedSubviews([button, buttonSpacerView])
        return stackView
    }()
    
    private lazy var contentStackView: CKStackView = {
        let stackView = CKStackView(spacing: 24)
        stackView.addArrangedSubviews([labelStackView, buttonStackView])
        stackView.isLayoutMarginsRelativeArrangement = true
        stackView.layoutMargins = .init(top: 16, left: 16, bottom: 16, right: 16)
        stackView.setBorderWidth(1)
        stackView.setBorderColor(ColorName.borderSoft200.color)
        stackView.setCornerRadius(16)
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()
    
    // MARK: - Members
    private weak var delegate: CKAddPersonInfoAndButtonViewDelegate?
    
    // MARK: - Initializers
    public init(delegate: CKAddPersonInfoAndButtonViewDelegate? = nil,
                title: String? = nil,
                info: String? = nil,
                buttonTitle: String? = nil,
                tag: Int = 0) {
        super.init(frame: .zero)
        setupView()
        bind(delegate: delegate,
             title: title,
             info: info,
             buttonTitle: buttonTitle,
             tag: tag)
    }
    
    required init?(coder: NSCoder) {
        super.init(coder: coder)
        setupView()
    }
    
    private func setupView() {
        addSubview(contentStackView)
        NSLayoutConstraint.activate([
            contentStackView.topAnchor.constraint(equalTo: topAnchor),
            contentStackView.leadingAnchor.constraint(equalTo: leadingAnchor),
            contentStackView.trailingAnchor.constraint(equalTo: trailingAnchor),
            contentStackView.bottomAnchor.constraint(equalTo: bottomAnchor)
        ])
    }
    
    // MARK: - Custom Methods
    public func bind(delegate: CKAddPersonInfoAndButtonViewDelegate? = nil,
                     title: String? = nil,
                     info: String? = nil,
                     buttonTitle: String? = nil,
                     tag: Int = 0) {
        self.delegate = delegate
        
        titleLabel.text = title
        titleLabel.isHidden = title?.isEmpty ?? true
        
        infoLabel.text = info
        infoLabel.isHidden = info?.isEmpty ?? true
        
        button.setTitle(buttonTitle ?? "")
        button.isHidden = buttonTitle?.isEmpty ?? true
        
        self.tag = tag
    }
    
    @objc private func didTappedButton(_ button: UIButton) {
        delegate?.didTappedCKAddPersonInfoAndButtonView(self.tag)
    }
}
