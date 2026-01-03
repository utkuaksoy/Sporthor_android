import UIKit
import DesignKit

public final class CKMissionTagView: UIView {
    private lazy var stackView: CKStackView = {
        let stackView = CKStackView(axis: .horizontal,
                                    alignment: .center,
                                    spacing: 5)
        stackView.translatesAutoresizingMaskIntoConstraints = false
        return stackView
    }()

    private lazy var colorView: UIView = {
        let view = UIView()
        view.setCornerRadius(4)
        return view
    }()

    private lazy var label: CKLabel = {
        let label = CKLabel(textColor: ColorName.contentStrong900.color,
                            font: .bold04Compact)
        return label
    }()

    public override init(frame: CGRect) {
        super.init(frame: frame)
        setupView()
    }

    required init?(coder: NSCoder) {
        super.init(coder: coder)
        setupView()
    }

    private func setupView() {
        stackView.addArrangedSubviews([colorView, label])
        addSubview(stackView)
        NSLayoutConstraint.activate([
            stackView.topAnchor.constraint(equalTo: topAnchor),
            stackView.bottomAnchor.constraint(equalTo: bottomAnchor),
            stackView.leadingAnchor.constraint(equalTo: leadingAnchor),
            stackView.trailingAnchor.constraint(equalTo: trailingAnchor),
            colorView.widthAnchor.constraint(equalToConstant: 8),
            colorView.heightAnchor.constraint(equalToConstant: 8)
        ])
    }

    public func bind(color: UIColor, text: String) {
        colorView.backgroundColor = color
        label.text = text
    }
}
