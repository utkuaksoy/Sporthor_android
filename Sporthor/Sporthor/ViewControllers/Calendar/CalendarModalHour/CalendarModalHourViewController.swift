//
//  CalendarModalHourViewController.swift
//  Sporthor
//
//  Created by derTurke on 28.05.2025.
//
//

import UIKit
import ComponentKit
import PanModal

final class CalendarModalHourViewController: BaseViewController {
    // MARK: - VIPER Variables
    var presenter: CalendarModalHourPresenterProtocol {
        get { return self.basePresenter as! CalendarModalHourPresenterProtocol }
        set { self.basePresenter = newValue }
    }
    
    // MARK: - UI Elements
    private lazy var scrollLineView: UIView = {
        let view = UIView()
        view.backgroundColor = DesignKitColorName.backgroundSub300.color
        view.setCornerRadius(2)
        return view
    }()
    
    private lazy var timePicker: UIDatePicker = {
        let picker = UIDatePicker()
        picker.datePickerMode = .time
        picker.preferredDatePickerStyle = .wheels
        picker.backgroundColor = .clear
        picker.locale = Locale(identifier: "tr_TR")
        picker.addTarget(self, action: #selector(timeChanged(_:)), for: .valueChanged)
        return picker
    }()
    
    private lazy var submitButton: CKButton = {
        let button = CKButton(delegate: self,
                              title: "Onayla",
                              titleColor: DesignKitColorName.contentStrong900.color,
                              buttonBackgroundColor: DesignKitColorName.backgroundPrimaryGreen.color,
                              cornerRadius: 23,
                              font: .bold03Compact,
                              tag: 3)
        button.heightAnchor.constraint(equalToConstant: 46).isActive = true
        return button
    }()
    
    // MARK: - Members
    
    // MARK: - Lifecycles
    override func viewDidLoad() {
        super.viewDidLoad()
        presenter.viewDidLoad()
    }
    
    // MARK: - Custom Methods
    
    @objc private func timeChanged(_ sender: UIDatePicker) {
        presenter.timeChanged(sender.date)
    }
}

// MARK: - CalendarModalHourPresenterDelegate
extension CalendarModalHourViewController: CalendarModalHourPresenterDelegate {
    func prepareUI() {
        [scrollLineView, timePicker, submitButton].forEach {
            $0.translatesAutoresizingMaskIntoConstraints = false
            view.addSubview($0)
        }
        NSLayoutConstraint.activate([
            scrollLineView.topAnchor.constraint(equalTo: view.safeAreaLayoutGuide.topAnchor, constant: 16),
            scrollLineView.centerXAnchor.constraint(equalTo: view.centerXAnchor),
            scrollLineView.widthAnchor.constraint(equalToConstant: 40),
            scrollLineView.heightAnchor.constraint(equalToConstant: 4),
            
            timePicker.topAnchor.constraint(equalTo: scrollLineView.bottomAnchor, constant: 16),
            timePicker.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 16),
            timePicker.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -16),
            timePicker.bottomAnchor.constraint(equalTo: submitButton.topAnchor, constant: -16),
            
            submitButton.leadingAnchor.constraint(equalTo: view.leadingAnchor, constant: 16),
            submitButton.trailingAnchor.constraint(equalTo: view.trailingAnchor, constant: -16),
            submitButton.bottomAnchor.constraint(equalTo: view.safeAreaLayoutGuide.bottomAnchor, constant: -16),
        ])
    }
    
    func didSetHour(_ date: Date) {
        DispatchQueue.main.async { [weak self] in
            guard let self = self else { return }
            self.timePicker.setDate(date, animated: true)
        }
    }
}

// MARK: - CKButtonDelegate
extension CalendarModalHourViewController: CKButtonDelegate {
    func ckButtonDidTap(tag: Int) {
        presenter.didTappedSubmitButton()
    }
}

// MARK: - PanModalPresentable
extension CalendarModalHourViewController: PanModalPresentable {
    var allowsExtendedPanScrolling: Bool {
        return true
    }
    
    var panScrollable: UIScrollView? {
        return nil
    }
    
    var longFormHeight: PanModalHeight {
        return .contentHeight(374)
    }
    
    var allowsDragToDismiss: Bool {
        return true
    }
    
    var allowsTapToDismiss: Bool {
        return true
    }
    
    var cornerRadius: CGFloat {
        return 16
    }
    
    var panModalBackgroundColor: UIColor {
        return .black.withAlphaComponent(0.4)
    }
    
    var showDragIndicator: Bool {
        return false
    }
}

